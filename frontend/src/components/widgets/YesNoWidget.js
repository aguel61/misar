function YesNoWidget({ question, value, onChange }) {
  return (
    <div className="widget yesno-widget">
      <span className="widget-label">{question.label}</span>
      <div className="yesno-buttons">
        <button
          type="button"
          className={`yesno-btn ${value === 'true' ? 'active' : ''}`}
          onClick={() => onChange('true')}
        >
          Yes
        </button>
        <button
          type="button"
          className={`yesno-btn ${value === 'false' ? 'active' : ''}`}
          onClick={() => onChange('false')}
        >
          No
        </button>
      </div>
    </div>
  );
}

export default YesNoWidget;
