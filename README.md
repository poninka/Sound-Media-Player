# Sound Media Player

A Java application with a graphical interface for playing local audio files and searching Spotify tracks. Developed as a collaborative project using Java, Maven, and the Spotify Web API.

## Features

- **Graphical User Interface**: Built with Java Swing, displaying song information, album artwork, and playback controls
- **Spotify Integration**: Integrated Spotify token authentication to allow users to search and view track information directly from the application using the Spotify Web API
- **Local File Playback**: Supports direct playback of local audio files, including .WAV and .MP3 formats
- **Cross-Platform**: Works on both Windows and macOS with proper file path handling

## Technologies

- **Java 21**: Core programming language
- **Maven**: Build automation and dependency management
- **Spotify Web API**: For track search and metadata retrieval
- **Java Swing**: For the graphical user interface
- **OkHttp**: HTTP client for API requests
- **Gson**: JSON parsing library
- **JLayer**: MP3 audio playback support

## Project Structure

```
FinalProject/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── soundmediaplayer/
│   │   │           ├── ui/              # UI components
│   │   │           ├── spotify/         # Spotify API integration
│   │   │           └── config/          # Configuration management
│   │   └── resources/
│   │       ├── assets/                  # Image resources
│   │       └── config.properties.example
│   └── test/
│       └── java/                        # Unit tests
├── pom.xml                              # Maven configuration
└── checkstyle.xml                       # Code quality configuration
```

## Setup Instructions

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Spotify Developer Account (for API credentials)

### Installation

1. Download the lastest release of SounMediaPlayer.jar run the program in the JVM.
Alternatively compile source by first cloning the repository:
   ```bash
   git clone https://github.com/poninka/Sound-Media-Player
   cd Sound-Media-Player/FinalProject
   ```
   Build the project:
   ```bash
   mvn clean compile
   ```

2. Run the application:
   ```bash
   java -jar Builds/SoundMediaPlayer.jar
   ```
   Or, from the repository root, you can launch it with Maven:
   ```bash
   java -jar Builds/SoundMediaPlayer.jar
   mvn exec:java -Dexec.mainClass="com.soundmediaplayer.ui.AudioManager"
   ```
   
3. Configure Spotify API credentials you can configure tokens at runtime using the "Configure API Tokens" button in the Spotify search UI

## Usage

### Playing Local Files

1. Launch the application
2. Click "Select File" to choose a local audio file (.WAV or .MP3)
3. Use the play/pause button to control playback
4. Use the slider to seek through the track

### Searching Spotify

1. Click the Spotify logo button in the main menu
2. Enter a song name in the search bar and press Enter
3. The application will display track information and album artwork
4. The track will open in your default browser

### Uploading Files

1. Click "Upload File" to copy audio files to the application's upload directory
2. Files will be stored in `AudioFiles/UploadedFiles/`

## Building and Testing

### Build
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Code Quality Check
```bash
mvn checkstyle:check
```

## Development

This project was developed collaboratively by a 5-person team using Git and GitHub for version control. The application demonstrates:

- Object-oriented design principles
- API integration and authentication
- Cross-platform file handling
- GUI development with Java Swing
- Build automation with Maven

## License

This project is provided as-is for educational and portfolio purposes.

## Acknowledgments

- Spotify Web API for track metadata
- JLayer library for MP3 playback support
