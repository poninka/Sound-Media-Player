import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Image;


public class LaunchUI extends JFrame {
    private static final long serialVersionUID = -1702351704098547978L;
	private JLabel titleDashArtist;
    private JLabel coverArtLabel;
    private JSlider songSeekSlider;

    public LaunchUI() {
        setTitle("Kevin From Accounting");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new java.awt.Color(0,0,0,220));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        titleDashArtist = new JLabel("Error");
        titleDashArtist.setForeground(Color.white);
        titleDashArtist.setHorizontalAlignment(SwingConstants.CENTER);
        titleDashArtist.setFont(new Font("Verdana", Font.BOLD, 20));
        
        coverArtLabel = new JLabel();
        coverArtLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        
        songSeekSlider = new JSlider();
        
        mainPanel.add(titleDashArtist, BorderLayout.NORTH); 
        mainPanel.add(coverArtLabel, BorderLayout.CENTER);
        mainPanel.add(songSeekSlider, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public void setSongInfo(String title, String artist, String coverArtPath, int songLength) {
    	titleDashArtist.setText(title + " - " + artist);
    	songSeekSlider.setMaximum(songLength);
    	ImageIcon icon = new ImageIcon(coverArtPath);
        Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        coverArtLabel.setIcon(new ImageIcon(image));
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	LaunchUI ui = new LaunchUI();
            ui.setSongInfo(/*"Song Title", "Artist Name",*/"The Walls", "Chase Alantic", "CoverArtDefault.jpeg", 200);
        });
    }
}
