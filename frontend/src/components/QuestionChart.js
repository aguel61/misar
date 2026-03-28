import {
  LineChart, Line, BarChart, Bar,
  XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer
} from 'recharts';

function SliderChart({ data }) {
  const chartData = data.map((p) => ({ date: p.date, value: parseInt(p.value, 10) }));
  return (
    <ResponsiveContainer width="100%" height={180}>
      <LineChart data={chartData}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="date" tick={{ fontSize: 11 }} />
        <YAxis domain={[1, 10]} tick={{ fontSize: 11 }} />
        <Tooltip />
        <Line type="monotone" dataKey="value" stroke="#6366f1" strokeWidth={2} dot={{ r: 3 }} />
      </LineChart>
    </ResponsiveContainer>
  );
}

function YesNoChart({ data }) {
  const yes = data.filter((p) => p.value === 'true').length;
  const no = data.filter((p) => p.value === 'false').length;
  const chartData = [{ name: 'Yes', count: yes }, { name: 'No', count: no }];
  return (
    <ResponsiveContainer width="100%" height={180}>
      <BarChart data={chartData}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" tick={{ fontSize: 11 }} />
        <YAxis allowDecimals={false} tick={{ fontSize: 11 }} />
        <Tooltip />
        <Bar dataKey="count" fill="#6366f1" radius={[4, 4, 0, 0]} />
      </BarChart>
    </ResponsiveContainer>
  );
}

function TextLog({ data }) {
  return (
    <ul className="text-log">
      {data.map((p, i) => (
        <li key={i} className="text-log-item">
          <span className="log-date">{p.date}</span>
          <span className="log-value">{p.value}</span>
        </li>
      ))}
    </ul>
  );
}

function QuestionChart({ question, data }) {
  const isEmpty = !data || data.length === 0;

  return (
    <div className="chart-card">
      <h3 className="chart-title">{question.label}</h3>
      <span className="chart-type-badge">{question.questionType}</span>
      {isEmpty ? (
        <p className="no-data">No data yet</p>
      ) : question.questionType === 'SLIDER' ? (
        <SliderChart data={data} />
      ) : question.questionType === 'YES_NO' ? (
        <YesNoChart data={data} />
      ) : (
        <TextLog data={data} />
      )}
    </div>
  );
}

export default QuestionChart;
