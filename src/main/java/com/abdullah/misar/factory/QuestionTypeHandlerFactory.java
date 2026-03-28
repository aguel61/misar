package com.abdullah.misar.factory;

import com.abdullah.misar.model.QuestionType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class QuestionTypeHandlerFactory {

    private final Map<QuestionType, QuestionTypeHandler> handlers;

    public QuestionTypeHandlerFactory(List<QuestionTypeHandler> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(QuestionTypeHandler::getType, Function.identity()));
    }

    public QuestionTypeHandler getHandler(QuestionType type) {
        QuestionTypeHandler handler = handlers.get(type);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for question type: " + type);
        }
        return handler;
    }
}
