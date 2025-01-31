import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class LinkPuller {
    private JFrame frame;
    private JPanel panel;
    private JPanel grid;
    private JButton goButton;
    private JTextField urlTextField;
    private JTextField searchTextField;
    private JTextArea resultsTextArea;

    public static void main(String[] args) {
        LinkPuller l = new LinkPuller();
    }

    public LinkPuller() {
        frame = new JFrame("LinkPuller");
        panel = new JPanel(new BorderLayout());
        grid = new JPanel(new GridLayout(2, 1));
        urlTextField = new JTextField();
        searchTextField = new JTextField();
        goButton = new JButton("Go!");
        resultsTextArea = new JTextArea();

        grid.add(urlTextField);
        grid.add(searchTextField);
        panel.add(grid, BorderLayout.NORTH);
        panel.add(goButton, BorderLayout.SOUTH);
        panel.add(resultsTextArea);

        //Add function to the go button
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlTextField.getText();
                String searchTerm = searchTextField.getText();
                searchLinks(url, searchTerm);
            }
        });

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setVisible(true);
        //Layout is done here
    }

    public void searchLinks(String url, String searchTerm) {
        try {
            URL website = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(website.openStream()));
            String line;
            String[] foundLinks = new String[100];
            int linkCount = 0;


            while ((line = reader.readLine()) != null) {
                if (line.contains("https:") && line.contains(searchTerm)) {
                    int start = line.indexOf("https:"); //start before the search term
                    while (start != -1) {
                        int end = line.indexOf(" ", start);
                        if(end == -1){
                            end = line.indexOf("\"", start);
                        }
                        String miniLine;
                        if (end != -1){
                            miniLine = line.substring(start, end);
                        }
                        else{
                            miniLine = line.substring(start); //in case line ends with link
                        }
                        //check for duplicate links
                        boolean findDuplicate = false;
                        for (int i = 0; i < linkCount; i++){
                            if(foundLinks[i].equals(miniLine)){
                                findDuplicate = true;
                                break;
                            }
                        }
                        if(findDuplicate == false){
                            foundLinks[linkCount] = miniLine;
                            linkCount++;
                            resultsTextArea.append(miniLine + "\n");
                        }

                        start = line.indexOf("https:", end);
                    }
                }
            }
            reader.close();

            //had an error so I just clicked resolve and this popped up
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}