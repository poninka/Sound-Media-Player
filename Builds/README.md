# Sound Media Player - Executable Build

This folder contains a standalone executable JAR file that can run on any system with Java installed.

## Requirements

- **Java 21 or higher** (JRE or JDK)
- No additional dependencies needed - all libraries are bundled in the JAR

## Running the Application

### On macOS/Linux:
```bash
java -jar SoundMediaPlayer.jar
```

### On Windows:
```bash
java -jar SoundMediaPlayer.jar
```

Or double-click the JAR file if Java is properly configured on your system.

## Building from Source

To rebuild this JAR file from the source code:

```bash
cd FinalProject
mvn clean package -Dcheckstyle.skip=true -DskipTests
```

The JAR will be created in the `../Builds/` directory.

## Features

- Play local audio files (.WAV and .MP3)
- Search Spotify tracks (requires API credentials)
- Upload files to the application
- Cross-platform support (Windows, macOS, Linux)

## Configuration

For Spotify API features, you can:
1. Create a `config.properties` file in the same directory as the JAR with your credentials
2. Or use the "Configure API Tokens" button in the Spotify search UI

Example `config.properties`:
```
spotify.client.id=your_client_id_here
spotify.client.secret=your_client_secret_here
```

## Troubleshooting

If you get a "Java not found" error:
- Make sure Java 21+ is installed
- Check your PATH environment variable includes Java
- Try running `java -version` to verify installation

If the application doesn't start:
- Make sure you're using Java 21 or higher
- Check that the JAR file isn't corrupted
- Try running from the command line to see error messages
