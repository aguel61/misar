import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  getActiveQuestions,
  getTodayCheckIn,
  getCheckInAnswers,
  submitCheckIn,
  resubmitCheckIn,
  resetTodayCheckIn,
} from '../services/api';
import { getWidget } from '../components/questionWidgetFactory';

function CheckInPage() {
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState({});
  const [isEdit, setIsEdit] = useState(false);   // true = updating an existing check-in
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    init();
  }, []);

  async function init() {
    setLoading(true);
    setError('');
    try {
      const qs = await getActiveQuestions();
      setQuestions(qs);

      // Build default answers
      const defaults = {};
      qs.forEach((q) => { defaults[q.id] = q.questionType === 'SLIDER' ? '5' : ''; });

      try {
        // Try to load today's existing check-in answers
        const today = await getTodayCheckIn();
        const existing = await getCheckInAnswers(today.id);

        // Pre-fill with existing values, fall back to defaults for new questions
        const prefilled = { ...defaults };
        existing.forEach((a) => { prefilled[a.questionId] = a.value; });

        setAnswers(prefilled);
        setIsEdit(true);
      } catch {
        // No check-in yet today — fresh form
        setAnswers(defaults);
        setIsEdit(false);
      }
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const payload = {
      answers: questions.map((q) => ({ questionId: q.id, value: answers[q.id] || '' })),
    };
    try {
      if (isEdit) {
        await resubmitCheckIn(payload);
      } else {
        await submitCheckIn(payload);
      }
      navigate('/');
    } catch (e) {
      setError(e.message);
    }
  }

  async function handleReset() {
    if (!window.confirm("Reset today's check-in? All answers for today will be deleted.")) return;
    try {
      await resetTodayCheckIn();
      await init();  // reload — form becomes fresh
    } catch (e) {
      setError(e.message);
    }
  }

  if (loading) return <div className="page"><p className="loading">Loading...</p></div>;

  if (questions.length === 0) {
    return (
      <div className="page center">
        <p>No active questions yet. <a href="/questions">Add some questions first.</a></p>
      </div>
    );
  }

  return (
    <div className="page">
      <div className="checkin-header">
        <div>
          <h2 className="page-title">
            {isEdit ? 'Update Today\'s Check-In' : 'Today\'s Check-In'}
          </h2>
          <p className="page-subtitle">
            {new Date().toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
          </p>
        </div>
        {isEdit && (
          <button className="btn btn-danger" type="button" onClick={handleReset}>
            Reset Today
          </button>
        )}
      </div>

      {isEdit && (
        <div className="info-banner">
          You already checked in today — you can update your answers below.
        </div>
      )}

      {error && <div className="error-banner">{error}</div>}

      <form className="checkin-form" onSubmit={handleSubmit}>
        {questions.map((q) => {
          const Widget = getWidget(q.questionType);
          return (
            <Widget
              key={q.id}
              question={q}
              value={answers[q.id]}
              onChange={(val) => setAnswers({ ...answers, [q.id]: val })}
            />
          );
        })}
        <button className="btn btn-primary btn-submit" type="submit">
          {isEdit ? 'Update Check-In' : 'Submit Check-In'}
        </button>
      </form>
    </div>
  );
}

export default CheckInPage;
