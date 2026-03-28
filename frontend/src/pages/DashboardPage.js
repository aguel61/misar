import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getActiveQuestions, getTodayCheckIn, getHistory } from '../services/api';
import QuestionChart from '../components/QuestionChart';

function DashboardPage() {
  const [questions, setQuestions] = useState([]);
  const [historyMap, setHistoryMap] = useState({});
  const [checkedInToday, setCheckedInToday] = useState(false);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    async function init() {
      try {
        await getTodayCheckIn();
        setCheckedInToday(true);
      } catch {
        setCheckedInToday(false);
      }

      try {
        const qs = await getActiveQuestions();
        setQuestions(qs);

        const histories = await Promise.all(
          qs.map((q) => getHistory(q.id, 30).catch(() => []))
        );
        const map = {};
        qs.forEach((q, i) => { map[q.id] = histories[i]; });
        setHistoryMap(map);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    }
    init();
  }, []);

  if (loading) return <div className="page"><p className="loading">Loading dashboard...</p></div>;

  return (
    <div className="page">
      <div className="dashboard-header">
        <div>
          <h2 className="page-title">Dashboard</h2>
          <p className="page-subtitle">Last 30 days</p>
        </div>
        {!checkedInToday && (
          <button className="btn btn-primary" onClick={() => navigate('/checkin')}>
            Submit Today's Check-In
          </button>
        )}
        {checkedInToday && (
          <div className="checked-in-actions">
            <span className="checked-in-badge">✓ Checked in today</span>
            <button className="btn btn-ghost btn-sm" onClick={() => navigate('/checkin')}>
              Update answers
            </button>
          </div>
        )}
      </div>

      {questions.length === 0 ? (
        <div className="empty-dashboard">
          <p>No questions set up yet.</p>
          <button className="btn btn-primary" onClick={() => navigate('/questions')}>
            Add Questions
          </button>
        </div>
      ) : (
        <div className="charts-grid">
          {questions.map((q) => (
            <QuestionChart key={q.id} question={q} data={historyMap[q.id] || []} />
          ))}
        </div>
      )}
    </div>
  );
}

export default DashboardPage;
