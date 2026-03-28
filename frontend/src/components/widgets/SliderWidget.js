function SliderWidget({ question, value, onChange }) {
  return (
    <div className="widget slider-widget">
      <div className="widget-header">
        <span className="widget-label">{question.label}</span>
        <span className="slider-value">{value || 5}</span>
      </div>
      <input
        type="range"
        min={1}
        max={10}
        value={value || 5}
        onChange={(e) => onChange(e.target.value)}
        className="slider-input"
      />
      <div className="slider-ticks">
        <span>1</span>
        <span>5</span>
        <span>10</span>
      </div>
    </div>
  );
}

export default SliderWidget;
