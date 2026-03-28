import SliderWidget from './widgets/SliderWidget';
import YesNoWidget from './widgets/YesNoWidget';
import TextWidget from './widgets/TextWidget';

const widgetMap = {
  SLIDER: SliderWidget,
  YES_NO: YesNoWidget,
  TEXT: TextWidget,
};

export function getWidget(questionType) {
  return widgetMap[questionType] ?? TextWidget;
}
