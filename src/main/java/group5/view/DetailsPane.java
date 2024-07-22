package group5.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.net.URL;

import javax.swing.text.StyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.List;
import java.util.ArrayList;

import group5.model.beans.MBeans;

import java.time.format.DateTimeFormatter; // For Date formatting

import group5.model.beans.MBeansLoader; // Testing

import javax.swing.UIManager; // Testing

import group5.model.formatters.MBeansLoader;

/**
 * A JPanel class to display details of a media.
 */
public class DetailsPane extends JPanel {

    /**
     * Date Formatter
     */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    /**
     * Default width.
     */
    private static final int DEFAULT_WIDTH = 300;

    /**
     * Default height.
     */
    private static final int DEFAULT_HEIGHT = 600;

    /**
     * Default color, light gray.
     */
    private static final Color DEFAULT_COLOR = new Color(230, 230, 230);

    /**
     * Holds scroll pane.
     */
    private JScrollPane scrollPane;

    /**
     * Holds details panel inside scroll pane.
     */
    private JPanel detailsPanel;

    /**
     * Holds TextPane object for media Title.
     */
    private JTextPane mediaTitle;

    /**
     * Holds media image Label obejct.
     */
    private JLabel mediaImage;

    /**
     * Holds lists of JTextArea for each media details.
     */
    private List<JTextArea> mediaDetails = new ArrayList<>();

    /**
     * Holds check box to display watched status.
     */
    private JCheckBox watchedBox;

    /**
     * Holds JTextField for user rating.
     */
    private JTextField userRating;

    /**
     * Holds JButton for updating user rating.
     */
    private JButton saveRating;

    /**
     * Constructor to create a DetailsPane object.
     *
     * Setup all the components contained inside this panel.
     */
    DetailsPane() {
        super();
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.detailsPanel = new JPanel();
        this.detailsPanel.setLayout(new BoxLayout(this.detailsPanel, BoxLayout.Y_AXIS));
        this.detailsPanel.setBackground(Color.WHITE);
        this.scrollPane = new JScrollPane(this.detailsPanel);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
        this.add(this.scrollPane, BorderLayout.CENTER);
        this.initContent();
    }

    /**
     * Initialize and setup all the components template contained inside this
     * panel.
     */
    private void initContent() {
        this.addTitlePane();
        this.addImageLabel();
        this.addWatched();
        this.addDetailPane("Released");
        this.addDetailPane("Type");
        this.addDetailPane("Rated");
        this.addDetailPane("Genre");
        this.addDetailPane("Runtime");
        this.addDetailPane("Director");
        this.addDetailPane("Actors");
        this.addDetailPane("Writer");
        this.addDetailPane("Plot");
        this.addDetailPane("Language");
        this.addDetailPane("Country");
        this.addDetailPane("Awards");
        this.addDetailPane("Metascore");
        this.addDetailPane("IMDB");
        this.addDetailPane("Box Office");
        this.addUserRating();

        this.scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Dynamically resize media title and media details.
                reSize();
            }
        });
    }

    /**
     * Add vertical padding to the details panel.
     *
     * @param padding the padding size to add.
     */
    private void addVerticalPadding(int padding) {
        this.detailsPanel.add(Box.createRigidArea(new Dimension(0, padding)));
    }

    /**
     * Add title pane to the details panel.
     */
    private void addTitlePane() {
        this.mediaTitle = new JTextPane();
        this.mediaTitle.setFont(new Font("Arial", Font.BOLD, 30));
        this.mediaTitle.setBackground(DEFAULT_COLOR);
        this.mediaTitle.setEditable(false);

        // Set word wrap for title pane.
        StyledDocument doc = this.mediaTitle.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        this.mediaTitle.setMinimumSize(new Dimension(DEFAULT_WIDTH, 30));
        this.detailsPanel.add(this.mediaTitle);
        this.addVerticalPadding(5);
    }

    /**
     * Add image label to the details panel.
     */
    private void addImageLabel() {
        this.mediaImage = new JLabel();
        this.mediaImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.mediaImage.setPreferredSize(new Dimension(200, 250));  // Scaling.
        this.mediaImage.setIcon(new ImageIcon("./src/main/resources/no-image.jpg"));
        this.detailsPanel.add(this.mediaImage);
        this.addVerticalPadding(5);
    }

    /**
     * Add watched check box to the details panel.
     */
    private void addWatched() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.watchedBox = new JCheckBox("Watched");
        this.watchedBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.watchedBox.setBackground(DEFAULT_COLOR);
        this.watchedBox.setFocusPainted(false);  // Remove border around text.
        this.detailsPanel.add(this.watchedBox);
        this.addVerticalPadding(5);
    }

    /**
     * Add detail pane to the details panel.
     *
     * @param name the name of the detail.
     */
    private void addDetailPane(String name) {
        // JPanel to encased both elements.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        // JPanel for detail title.
        JLabel label = new JLabel(name + ": ");
        label.setFont(new Font("Dialog", Font.PLAIN, 12));
        label.setPreferredSize(new Dimension(75, 10));
        label.setVerticalAlignment(JLabel.TOP);
        label.setBorder(BorderFactory.createEmptyBorder(2, 4, 0, 0));

        // JTextArea for actual media details.
        JTextArea text = new JTextArea();
        text.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 6));
        text.setFont(new Font("Dialog", Font.PLAIN, 12));
        text.setBackground(new Color(230, 230, 230));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setMinimumSize(new Dimension(150, 20));
        this.mediaDetails.add(text);

        panel.add(label, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);
        this.detailsPanel.add(panel);
        this.addVerticalPadding(5);
    }

    /**
     * Add user rating to the details panel.
     */
    private void addUserRating() {
        // JPanel to encased both elements.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        // JPanel for detail title.
        JLabel label = new JLabel("My Rating: ");
        label.setFont(new Font("Dialog", Font.PLAIN, 12));
        label.setPreferredSize(new Dimension(75, 10));
        label.setVerticalAlignment(JLabel.TOP);
        label.setBorder(BorderFactory.createEmptyBorder(2, 4, 0, 0));

        // JTextArea for actual media details.
        this.userRating = new JTextField();
        this.userRating.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 6));
        this.userRating.setFont(new Font("Dialog", Font.PLAIN, 12));
        this.userRating.setBackground(new Color(230, 230, 230));
        this.userRating.setEditable(true);
        this.userRating.setMinimumSize(new Dimension(150, 20));

        // JButton to update user rating.
        this.saveRating = new JButton("Save");
        this.saveRating.setPreferredSize(new Dimension(75, 10));
        this.saveRating.setFocusPainted(false);  // Remove border around text.

        panel.add(label, BorderLayout.WEST);
        panel.add(this.userRating, BorderLayout.CENTER);
        panel.add(this.saveRating, BorderLayout.EAST);
        this.detailsPanel.add(panel);
        this.addVerticalPadding(5);
    }

    /**
     * Resize the media title and media details.
     *
     * Allows the media title and media details to be resized dynamically when
     * the window is resized.
     */
    private void reSize() {
        this.mediaTitle.setPreferredSize(null);
        this.mediaTitle.setSize(this.scrollPane.getSize());
        this.mediaTitle.setPreferredSize(new Dimension(this.scrollPane.getSize().width - 22,
                this.mediaTitle.getPreferredSize().height));
        for (JTextArea text : mediaDetails) {
            text.setSize(new Dimension(this.scrollPane.getWidth() - 114, text.getHeight()));
            this.scrollPane.revalidate();
        }
    }

    /**
     * Scale the image to fit the media image label.
     *
     * @param imgUrl the URL of the image to scale.
     * @return the scaled image.
     */
    private ImageIcon scaleImage(URL imgUrl) {
        try {
            BufferedImage imageBig = ImageIO.read(imgUrl);
            Image image = imageBig.getScaledInstance(200, 250, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImageIcon("./data/no-image.jpg");
    }

    /**
     * Set the media details to display.
     *
     * @param media the media to display.
     */
    public void setMedia(MBeans media) {
        this.mediaTitle.setText(media.getTitle());
        this.mediaImage.setIcon(this.scaleImage(media.getPoster()));
        this.mediaDetails.get(0).setText(DATE_FORMAT.format(media.getReleased()));
        this.mediaDetails.get(1).setText(media.getType());
        this.mediaDetails.get(2).setText(media.getRated());
        this.mediaDetails.get(3).setText(media.getGenre());
        this.mediaDetails.get(4).setText(Integer.toString(media.getRuntime()) + " minutes");
        this.mediaDetails.get(5).setText(String.join("\n", media.getDirector()));
        this.mediaDetails.get(6).setText(String.join("\n", media.getActors()));
        this.mediaDetails.get(7).setText(String.join("\n", media.getWriter()));
        this.mediaDetails.get(8).setText(media.getPlot());
        this.mediaDetails.get(9).setText(String.join("\n", media.getLanguage()));
        this.mediaDetails.get(10).setText(String.join("\n", media.getCountry()));
        this.mediaDetails.get(11).setText(media.getAwards());
        this.mediaDetails.get(12).setText(Integer.toString(media.getMetascore()));
        this.mediaDetails.get(13).setText(Double.toString(media.getImdbRating()));
        this.mediaDetails.get(14).setText(media.getBoxOffice().toString());
        this.userRating.setText(Double.toString(media.getMyRating()));
        this.watchedBox.setSelected(media.getWatched());
        SwingUtilities.invokeLater(() -> {
            this.reSize();
            this.scrollPane.getVerticalScrollBar().setValue(0); // Move to top
        });
    }

    /**
     * Add listeners to the watched check box and save rating button.
     */
    public void addListeners() {
        // TODO: Add listeners method for watchedBox and saveRating.
        //this.watchedBox.addActionListener(new ActionListener() {
        //    @Override
        //    public void actionPerformed(ActionEvent e) {
        //        System.out.println("Watched: " + watchedBox.isSelected());
        //    }
        //});
        //
        //this.saveRating.addActionListener(new ActionListener() {
        //    @Override
        //    public void actionPerformed(ActionEvent e) {
        //        System.out.println("User Rating: " + userRating.getText());
        //    }
        //});
    }

    /**
     * Main method to test the DetailsPane.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 600);
        DetailsPane detailsPane = new DetailsPane();
        frame.add(detailsPane);
        MBeans media = MBeansLoader.loadMBeansFromAPI("The Matrix", "", "");
        detailsPane.setMedia(media);
        frame.setVisible(true);
    }
}
