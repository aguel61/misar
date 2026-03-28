function TextWidget({ question, value, onChange }) {
  return (
    <div className="widget text-widget">
      <label className="widget-label">{question.label}</label>
      <textarea
        value={value || ''}
        onChange={(e) => onChange(e.target.value)}
        maxLength={500}
        rows={3}
        placeholder="Type your answer..."
        className="text-input"
      />
      <span className="char-count">{(value || '').length}/500</span>
    </div>
  );
}

export default TextWidget;
