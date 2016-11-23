package com.github.maksmshn.blackjack_client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.TextField;

/**
 * Adapted from: http://stackoverflow.com/a/18959399.
 * Allows to type in only floats with at most two digits after dot
 * (2, 2., 2.0, 2.11, .99, .01) are all allowed.
 * (2.3343, 0.33434, .233) are not allowed
 */
public class NumberTextField extends TextField {
	private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void replaceText(int start, int end, String text) {
    	logger.trace("Entered text in NumberTextField: {}",text);
        if (validate(this.getText(), start, end, text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
    	if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String oldText, int start, int end, String inputText) {
    	String newText = oldText.substring(0, start) +
    			inputText + oldText.substring(end);
    	return validate(newText);
    }

    private boolean validate(String text) {
    	logger.trace("Validating text in NumberTextField: {}", text);
    	if (!text.isEmpty()) {
    		return text.matches("\\d*(\\.\\d{0,2})?");
    	} else {
    		return true;
    	}
    }
}

