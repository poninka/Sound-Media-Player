package com.soundmediaplayer.ui.components;

import javax.swing.JSlider;

/**
 * Slider that can update from playback progress without triggering user-facing events.
 */
public class PlaybackSlider extends JSlider {
    private static final long serialVersionUID = 1L;
    private boolean programmaticChange;

    public PlaybackSlider() {
        super();
        setOpaque(false);
    }

    /**
     * Updates the slider value from code without notifying listeners.
     */
    public void setProgrammaticValue(int value) {
        programmaticChange = true;
        try {
            super.setValue(value);
        } finally {
            programmaticChange = false;
        }
    }

    /**
     * Returns true if the slider is currently being updated from code.
     */
    public boolean isProgrammaticChange() {
        return programmaticChange;
    }
}
