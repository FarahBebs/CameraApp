# CameraApp




Webcam Initialization and Image Capture:

The application uses the Sarxos Webcam library to capture images from a webcam.
The takePictureButton button opens the webcam, captures an image, and displays it in the GUI.
Filter Buttons and Image Processing:

The application provides three filter buttons: brightnessFilterButton, blackAndWhiteFilterButton, and dimmingFilterButton.
Each filter button applies a specific image filter to the captured image.
The image processing is performed by modifying the RGB values of each pixel in the image.
Saving the Filtered Image:

The savePictureButton button allows the user to save the filtered image.
It opens a file chooser dialog where the user can select a directory to save the image.
The filtered image is saved as "output.jpg" in the selected directory.
Before saving, the code checks if the file already exists and prompts the user for confirmation to overwrite it.
Image Sharing on Social Media:

The sharePictureButton button allows the user to share the captured image on Twitter, Reddit, or Imgur.
It displays a dialog box with sharing options.
When a sharing option is selected, the code asynchronously uploads the image to Imgur using the uploadToImgur method.
The returned image link is used to create a sharing URL for the selected social media platform.
The URL is opened in the user's default web browser using the Desktop.browse method.
Imgur Image Upload:

The uploadToImgur method uploads the filtered image to Imgur using the Imgur API.
It reads the image file, encodes it as Base64, and sends an HTTP POST request to the Imgur API endpoint.
The response is parsed to extract the uploaded image link.
