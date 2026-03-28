const BASE = '';

async function request(path, options = {}) {
  const res = await fetch(`${BASE}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (res.status === 204) return null;
  const data = await res.json();
  if (!res.ok) throw new Error(data.error || 'Request failed');
  return data;
}

// Questions
export const getQuestions = () => request('/api/questions');
export const getActiveQuestions = () => request('/api/questions/active');
export const createQuestion = (body) => request('/api/questions', { method: 'POST', body: JSON.stringify(body) });
export const updateQuestion = (id, body) => request(`/api/questions/${id}`, { method: 'PUT', body: JSON.stringify(body) });
export const toggleQuestion = (id) => request(`/api/questions/${id}/toggle`, { method: 'PATCH' });
export const deleteQuestion = (id) => request(`/api/questions/${id}`, { method: 'DELETE' });

// Check-ins
export const getCheckIns = () => request('/api/checkins');
export const getTodayCheckIn = () => request('/api/checkins/today');
export const submitCheckIn = (body) => request('/api/checkins', { method: 'POST', body: JSON.stringify(body) });
export const resubmitCheckIn = (body) => request('/api/checkins/today', { method: 'PUT', body: JSON.stringify(body) });
export const resetTodayCheckIn = () => request('/api/checkins/today', { method: 'DELETE' });
export const getCheckInAnswers = (id) => request(`/api/checkins/${id}/answers`);

// Dashboard
export const getHistory = (questionId, days = 30) =>
  request(`/api/dashboard/history?questionId=${questionId}&days=${days}`);
