import { useEffect, useState } from 'react';
import { getQuestions, createQuestion, updateQuestion, toggleQuestion, deleteQuestion } from '../services/api';

const EMPTY_FORM = { label: '', questionType: 'SLIDER', orderIndex: 0 };

function QuestionsPage() {
  const [questions, setQuestions] = useState([]);
  const [form, setForm] = useState(EMPTY_FORM);
  const [editId, setEditId] = useState(null);
  const [editForm, setEditForm] = useState(EMPTY_FORM);
  const [error, setError] = useState('');

  useEffect(() => {
    loadQuestions();
  }, []);

  async function loadQuestions() {
    try {
      setQuestions(await getQuestions());
    } catch (e) {
      setError(e.message);
    }
  }

  async function handleCreate(e) {
    e.preventDefault();
    if (!form.label.trim()) return;
    try {
      await createQuestion(form);
      setForm(EMPTY_FORM);
      await loadQuestions();
    } catch (e) {
      setError(e.message);
    }
  }

  async function handleUpdate(id) {
    try {
      await updateQuestion(id, editForm);
      setEditId(null);
      await loadQuestions();
    } catch (e) {
      setError(e.message);
    }
  }

  async function handleToggle(id) {
    try {
      await toggleQuestion(id);
      await loadQuestions();
    } catch (e) {
      setError(e.message);
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this question?')) return;
    try {
      await deleteQuestion(id);
      await loadQuestions();
    } catch (e) {
      setError(e.message);
    }
  }

  function startEdit(q) {
    setEditId(q.id);
    setEditForm({ label: q.label, questionType: q.questionType, orderIndex: q.orderIndex });
  }

  return (
    <div className="page">
      <h2 className="page-title">Manage Questions</h2>

      {error && <div className="error-banner">{error}</div>}

      <form className="add-form" onSubmit={handleCreate}>
        <input
          className="input"
          placeholder="Question label"
          value={form.label}
          onChange={(e) => setForm({ ...form, label: e.target.value })}
          required
        />
        <select className="input" value={form.questionType} onChange={(e) => setForm({ ...form, questionType: e.target.value })}>
          <option value="SLIDER">Slider (1-10)</option>
          <option value="YES_NO">Yes / No</option>
          <option value="TEXT">Text</option>
        </select>
        <input
          className="input input-sm"
          type="number"
          min={0}
          placeholder="Order"
          value={form.orderIndex}
          onChange={(e) => setForm({ ...form, orderIndex: parseInt(e.target.value, 10) || 0 })}
        />
        <button className="btn btn-primary" type="submit">Add Question</button>
      </form>

      <table className="questions-table">
        <thead>
          <tr>
            <th>Label</th>
            <th>Type</th>
            <th>Order</th>
            <th>Active</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {questions.map((q) => (
            <tr key={q.id} className={q.active ? '' : 'row-inactive'}>
              {editId === q.id ? (
                <>
                  <td><input className="input" value={editForm.label} onChange={(e) => setEditForm({ ...editForm, label: e.target.value })} /></td>
                  <td>
                    <select className="input" value={editForm.questionType} onChange={(e) => setEditForm({ ...editForm, questionType: e.target.value })}>
                      <option value="SLIDER">Slider</option>
                      <option value="YES_NO">Yes/No</option>
                      <option value="TEXT">Text</option>
                    </select>
                  </td>
                  <td><input className="input input-sm" type="number" value={editForm.orderIndex} onChange={(e) => setEditForm({ ...editForm, orderIndex: parseInt(e.target.value, 10) || 0 })} /></td>
                  <td>-</td>
                  <td>
                    <button className="btn btn-primary btn-sm" onClick={() => handleUpdate(q.id)}>Save</button>
                    <button className="btn btn-ghost btn-sm" onClick={() => setEditId(null)}>Cancel</button>
                  </td>
                </>
              ) : (
                <>
                  <td>{q.label}</td>
                  <td><span className={`badge badge-${q.questionType.toLowerCase()}`}>{q.questionType}</span></td>
                  <td>{q.orderIndex}</td>
                  <td>
                    <input type="checkbox" checked={q.active} onChange={() => handleToggle(q.id)} />
                  </td>
                  <td>
                    <button className="btn btn-ghost btn-sm" onClick={() => startEdit(q)}>Edit</button>
                    <button className="btn btn-danger btn-sm" onClick={() => handleDelete(q.id)}>Delete</button>
                  </td>
                </>
              )}
            </tr>
          ))}
          {questions.length === 0 && (
            <tr><td colSpan={5} className="empty-row">No questions yet. Add one above!</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default QuestionsPage;
