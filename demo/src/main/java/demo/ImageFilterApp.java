package demo;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import java.net.URL;

public class ImageFilterApp extends JFrame {

    private BufferedImage image;
    private JLabel photoLabel;
    private File lastSelectedDirectory;

    public ImageFilterApp() {
        setTitle("Image Filter App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Create buttons
        JButton brightnessFilterButton = new JButton("Brightness Filter");
        JButton blackAndWhiteFilterButton = new JButton("Black and White Filter");
        JButton dimmingFilterButton = new JButton("Dimming Filter");
        JButton takePictureButton = new JButton("Take Picture");
        JButton savePictureButton = new JButton("Save Picture");
        JButton sharePictureButton = new JButton("Share Picture");

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(brightnessFilterButton);
        buttonPanel.add(blackAndWhiteFilterButton);
        buttonPanel.add(dimmingFilterButton);

        // Create control panel
        JPanel controlPanel = new JPanel(new GridLayout(3, 1));
        controlPanel.add(takePictureButton);
        controlPanel.add(savePictureButton);
        controlPanel.add(sharePictureButton);

        // Add panels to the main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.WEST);

        // Add button listeners
        brightnessFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Apply brightness filter to image
                if (image != null) {
                    // Modify the image pixels to adjust brightness
                    BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(),
                            BufferedImage.TYPE_INT_RGB);

                    // Adjust the brightness value (between -255 to 255)
                    int brightnessAdjustment = 50; // Example: Increase brightness by 50 units

                    // Apply brightness adjustment to each pixel
                    for (int y = 0; y < image.getHeight(); y++) {
                        for (int x = 0; x < image.getWidth(); x++) {
                            Color originalColor = new Color(image.getRGB(x, y));
                            int red = originalColor.getRed() + brightnessAdjustment;
                            int green = originalColor.getGreen() + brightnessAdjustment;
                            int blue = originalColor.getBlue() + brightnessAdjustment;

                            // Ensure that color values are within the valid range of 0-255
                            red = Math.min(Math.max(red, 0), 255);
                            green = Math.min(Math.max(green, 0), 255);
                            blue = Math.min(Math.max(blue, 0), 255);

                            Color adjustedColor = new Color(red, green, blue);
                            filteredImage.setRGB(x, y, adjustedColor.getRGB());
                        }
                    }

                    // Update the displayed image with the filtered image
                    photoLabel.setIcon(new ImageIcon(filteredImage));
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "Brightness filter applied successfully!");
                } else {
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "No image available!");
                }
            }
        });

        blackAndWhiteFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Apply black and white filter to image
                if (image != null) {
                    // Convert the image to black and white
                    BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(),
                            BufferedImage.TYPE_BYTE_BINARY);
                    Graphics2D graphics = filteredImage.createGraphics();
                    graphics.drawImage(image, 0, 0, null);
                    graphics.dispose();

                    // Update the displayed image with the filtered image
                    photoLabel.setIcon(new ImageIcon(filteredImage));
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "Black and white filter applied successfully!");
                } else {
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "No image available!");
                }
            }
        });

        dimmingFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Apply dimming filter to image
                if (image != null) {
                    // Modify the image pixels to adjust brightness (dimming effect)
                    BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(),
                            BufferedImage.TYPE_INT_RGB);

                    // Adjust the dimming factor (between 0.0 to 1.0)
                    double dimmingFactor = 0.5; // Example: Apply 50% dimming

                    // Apply dimming effect to each pixel
                    for (int y = 0; y < image.getHeight(); y++) {
                        for (int x = 0; x < image.getWidth(); x++) {
                            Color originalColor = new Color(image.getRGB(x, y));
                            int red = (int) (originalColor.getRed() * dimmingFactor);
                            int green = (int) (originalColor.getGreen() * dimmingFactor);
                            int blue = (int) (originalColor.getBlue() * dimmingFactor);

                            Color dimmedColor = new Color(red, green, blue);
                            filteredImage.setRGB(x, y, dimmedColor.getRGB());
                        }
                    }

                    // Update the displayed image with the filtered image
                    photoLabel.setIcon(new ImageIcon(filteredImage));
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "Dimming filter applied successfully!");
                } else {
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "No image available!");
                }
            }
        });

        takePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Take a picture using a camera
                Webcam webcam = Webcam.getDefault();
                webcam.setViewSize(WebcamResolution.VGA.getSize());
                webcam.open();

                if (webcam.isOpen()) {
                    image = webcam.getImage();
                    webcam.close();
                    photoLabel.setIcon(new ImageIcon(image));
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "Picture taken successfully!");
                } else {
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "Failed to open webcam!");
                }
            }
        });

        savePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save the filtered image
                if (photoLabel.getIcon() != null) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save Image");
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    // If the last selected directory is available, set it as the current directory
                    if (lastSelectedDirectory != null) {
                        fileChooser.setCurrentDirectory(lastSelectedDirectory);
                    }

                    int userSelection = fileChooser.showSaveDialog(ImageFilterApp.this);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File selectedDirectory = fileChooser.getSelectedFile();
                        lastSelectedDirectory = selectedDirectory; // Update the last selected directory

                        File outputFile = new File(selectedDirectory, "output.jpg");

                        try {
                            Image filteredImage = ((ImageIcon) photoLabel.getIcon()).getImage();
                            BufferedImage bufferedImage = new BufferedImage(filteredImage.getWidth(null),
                                    filteredImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2d = bufferedImage.createGraphics();
                            g2d.drawImage(filteredImage, 0, 0, null);
                            g2d.dispose();

                            // Check if the file already exists
                            if (outputFile.exists()) {
                                int overwriteConfirmation = JOptionPane.showConfirmDialog(
                                        ImageFilterApp.this, "The file already exists. Do you want to overwrite it?",
                                        "File Exists", JOptionPane.YES_NO_OPTION);
                                if (overwriteConfirmation == JOptionPane.NO_OPTION) {
                                    return; // Don't save the file if not confirmed to overwrite
                                }
                            }

                            ImageIO.write(bufferedImage, "jpg", outputFile);

                            // Save a copy of the image in the project directory
                            File projectDirectory = new File(System.getProperty("user.dir"));
                            File projectOutputFile = new File(projectDirectory, "output.jpg");
                            ImageIO.write(bufferedImage, "jpg", projectOutputFile);

                            JOptionPane.showMessageDialog(ImageFilterApp.this, "Filtered image saved successfully!");
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(ImageFilterApp.this, "Failed to save filtered image!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "No image available!");
                }
            }
        });

        sharePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Share the captured image
                if (image != null) {
                    // Show sharing options dialog
                    String[] options = { "Twitter", "Reddit", "Imgur" };
                    int choice = JOptionPane.showOptionDialog(ImageFilterApp.this, "Share on:", "Share Image",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                    if (choice == 0) {
                        // Share on Twitter
                        shareOnTwitter();
                    } else if (choice == 1) {
                        // Share on reddit
                        shareOnReddit();
                    } else if (choice == 2) {
                        // Share on Imgur
                        shareOnImgur();
                    }
                } else {
                    JOptionPane.showMessageDialog(ImageFilterApp.this, "No image available!");
                }
            }
        });

        // Create photo placeholder
        photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(photoLabel, BorderLayout.CENTER);
    }

    private String uploadToImgur() {
        
        String clientId = "4d57eea4c9f4dd7";

        try {
            //  Upload the image to Imgur
            String imagePath = "output.jpg"; // Assuming the filtered image is saved as "output.jpg"

            String imageLink = uploadImageToImgur(imagePath, clientId);

            // Save the image link in a variable or use it as needed
            System.out.println("Image link: " + imageLink);

            return imageLink;
        } catch (IOException e) {

            return "";
        }

    }

    private String uploadImageToImgur(String imagePath, String clientId) throws IOException {
        try {
            HttpClient httpClient = HttpClients.createDefault();

            // Read image file content
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);

            // Encode image bytes as Base64
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

            // Prepare API request
            HttpPost httpPost = new HttpPost("https://api.imgur.com/3/image");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Client-ID " + clientId);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

            // Create request body
            StringEntity requestBody = new StringEntity(
                    "{\"image\":\"" + imageBase64 + "\"}",
                    ContentType.APPLICATION_JSON);
            httpPost.setEntity(requestBody);

            // Send API request
            HttpResponse response = httpClient.execute(httpPost);

            // Handle API response
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();

            if (statusCode == 200 && responseEntity != null) {
                String responseString = EntityUtils.toString(responseEntity);
                System.out.println("Image uploaded to Imgur: " + responseString);
                JSONObject jsonResponse = new JSONObject(responseString);
                String imageLink = jsonResponse.getJSONObject("data").getString("link");
               

                return imageLink; // Return the image link
            } else {
                System.out.println("Image upload to Imgur failed. Status code: " + statusCode);
                
            }
        } catch (Exception e) {
            throw new IOException("Error uploading the image to Imgur.", e);
        }

        return null; // Return null if image upload fails
    }

    // Share on Twitter
    private void shareOnTwitter() {
        // Start the image upload process asynchronously
        CompletableFuture<String> imageUploadFuture = CompletableFuture
                .supplyAsync(new java.util.function.Supplier<String>() {
                    @Override
                    public String get() {
                        return uploadToImgur();
                    }
                });
        // Wait for the image upload to complete

        String imageLink;
        try {
            imageLink = imageUploadFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String twitterIntentURL = "https://twitter.com/intent/tweet?text=check%20out%20this%20cool%20image%20"
                + imageLink;

        try {
            URI uri = new URI(twitterIntentURL);
            URL url = uri.toURL();
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(url.toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void shareOnImgur() {
        // Start the image upload process asynchronously
        CompletableFuture<String> imageUploadFuture = CompletableFuture
                .supplyAsync(new java.util.function.Supplier<String>() {
                    @Override
                    public String get() {
                        return uploadToImgur();
                    }
                });
        // Wait for the image upload to complete

        String imageLink;
        try {
            imageLink = imageUploadFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            URI uri = new URI(imageLink);
            URL url = uri.toURL();
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(url.toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // Share on Reddit
    private void shareOnReddit() {
        // Start the image upload process asynchronously
        CompletableFuture<String> imageUploadFuture = CompletableFuture
                .supplyAsync(new java.util.function.Supplier<String>() {
                    @Override
                    public String get() {
                        return uploadToImgur();
                    }
                });
        // Wait for the image upload to complete

        String imageLink;
        try {
            imageLink = imageUploadFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String redditIntentURL = "http://www.reddit.com/submit?url=" + imageLink
                + "&title=check%20out%20this%20cool%20image%20";

        try {
            URI uri = new URI(redditIntentURL);
            URL url = uri.toURL();
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(url.toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageFilterApp().setVisible(true);
            }
        });
    }
}