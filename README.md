# Web-Receipt-App

## How to run the application ##
1. Open the root folder (*webapptest*) with your chosen IDE.
2. In the root folder, navigate through the following folder path:
    **webapptest** -> **src** -> **main** -> **java/com/example/webapptest**
3. Click on *WebapptestApplication.java* and click the "Run Java" button within your IDE. Wait for the terminal console to show that the Spring Boot server has started sucessfully.
4. Open the Google Chrome browser and go to this link:
   http://localhost:8080/index.html
   
*Note : Physical Receipt Screenshots must be refined using a CamScanner first in order to be properly extracted.*

## Models used for this application ##
- Tesseract OCR (via Tess4J), responsible for reading image files and extracting them into text.
- LLM DeepSeek-r1:1.5b, responsible for sorting the extracted file into their respective fields.
