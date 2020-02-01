package com.stanislavgrujic.documentimporter.parsing;

interface State {
  State handle(Event event);
}
