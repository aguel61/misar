package com.abdullah.misar.factory;

import com.abdullah.misar.model.QuestionType;
import org.springframework.stereotype.Component;

@Component
public class SliderHandler implements QuestionTypeHandler {

    @Override
    public QuestionType getType() {
        return QuestionType.SLIDER;
    }

    @Override
    public String validate(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return "Value must not be blank";
        }
        try {
            int val = Integer.parseInt(rawValue.trim());
            if (val < 1 || val > 10) {
                return "Slider value must be between 1 and 10";
            }
        } catch (NumberFormatException e) {
            return "Slider value must be a whole number";
        }
        return null;
    }

    @Override
    public String normalize(String rawValue) {
        return rawValue.trim();
    }
}
