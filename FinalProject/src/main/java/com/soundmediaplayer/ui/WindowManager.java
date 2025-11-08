package com.soundmediaplayer.ui;

import javax.swing.JFrame;

/**
 * Manages window instances to enable UI swapping without dispose/create.
 */
public class WindowManager {
    private static JFrame currentWindow;
    private static AudioManager audioManager;
    private static LaunchUI launchUI;
    private static SpotifyLaunchUI spotifyLaunchUI;

    /**
     * Sets the current active window.
     */
    public static void setCurrentWindow(JFrame window) {
        if (currentWindow != null && currentWindow != window) {
            currentWindow.setVisible(false);
        }
        currentWindow = window;
        if (window != null) {
            window.setVisible(true);
            window.toFront();
            window.requestFocus();
        }
    }

    /**
     * Gets or creates the AudioManager instance.
     */
    public static AudioManager getAudioManager() {
        if (audioManager == null) {
            audioManager = new AudioManager();
        }
        return audioManager;
    }

    /**
     * Gets or creates the LaunchUI instance.
     */
    public static LaunchUI getLaunchUI() {
        if (launchUI == null) {
            launchUI = new LaunchUI();
        }
        return launchUI;
    }

    /**
     * Gets or creates the SpotifyLaunchUI instance.
     */
    public static SpotifyLaunchUI getSpotifyLaunchUI() {
        if (spotifyLaunchUI == null) {
            spotifyLaunchUI = new SpotifyLaunchUI();
        }
        return spotifyLaunchUI;
    }

    /**
     * Switches to AudioManager window.
     */
    public static void showAudioManager() {
        AudioManager am = getAudioManager();
        setCurrentWindow(am);
        am.setLocationRelativeTo(null);
    }

    /**
     * Switches to LaunchUI window.
     */
    public static void showLaunchUI() {
        LaunchUI ui = getLaunchUI();
        setCurrentWindow(ui);
        ui.setLocationRelativeTo(null);
    }

    /**
     * Switches to SpotifyLaunchUI window.
     */
    public static void showSpotifyLaunchUI() {
        SpotifyLaunchUI ui = getSpotifyLaunchUI();
        setCurrentWindow(ui);
        ui.setLocationRelativeTo(null);
    }
}

