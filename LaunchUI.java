import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class LaunchUI extends JFrame {
    private static final long serialVersionUID = -1702351704098547978L;
	private JLabel titleLabel;
    private JLabel artistLabel;
    private JLabel coverArtLabel;

    public LaunchUI() {
        setTitle("Music Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("Title: ");
        artistLabel = new JLabel("Artist: ");
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(artistLabel, BorderLayout.CENTER);

        coverArtLabel = new JLabel();
        coverArtLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(coverArtLabel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public void setSongInfo(String title, String artist, Image coverArt) {
        titleLabel.setText("Title: " + title);
        artistLabel.setText("Artist: " + artist);
        coverArtLabel.setIcon(new ImageIcon(coverArt.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LaunchUI();
        });
    }
}
