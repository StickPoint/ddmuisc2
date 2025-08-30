# DdMusic Music Player

DdMusic is a modern desktop music player application developed with JavaFX, providing a smooth user experience and rich music playback features.

## Project Overview

DdMusic adopts a modular design, building the user interface through the JavaFX framework. It supports core functions such as local music playback, playlist management, and volume control. The project structure is clear, with code following good object-oriented design principles for easy maintenance and extension.

## Core Features

### 1. Music Playback Control
- **Play/Pause Control**: Supports basic play and pause functionality
- **Previous/Next Track**: Quick switching between music tracks
- **Progress Control**: Supports dragging the progress bar for precise playback positioning
- **Volume Adjustment**: Pop-up volume slider for adjusting playback volume
- **Playback Status Synchronization**: Automatic synchronization of playback status with interface icons

### 2. User Interface Design
- **Modern Interface**: Flat design style with a clean and beautiful interface
- **Responsive Layout**: Supports window resizing and automatically adapts to different resolutions
- **Menu Navigation**: Left-side navigation menu supporting categories like Discover Music and My Music
- **Bottom Playback Control Bar**: Fixed bottom playback control area for convenient access

### 3. Audio Processing
- **Multimedia Support**: Audio playback implementation based on JavaFX MediaPlayer
- **Format Compatibility**: Supports mainstream audio formats (MP3, etc.)
- **Resource Management**: Complete MediaPlayer resource release mechanism
- **Playlist**: Supports music list management and playback queue control

## Technical Architecture

### 1. Core Components
- **HomePage**: Main page container integrating various functional modules
- **BottomMusicContainer**: Bottom music playback control bar containing playback controls, progress bar, and volume control
- **HomePageMenuItem**: Left navigation menu item supporting ToggleGroup single selection mechanism
- **SvgIcon**: Custom SVG icon component supporting dynamic content modification

### 2. Custom Controls
- **RedVerticalSliderSkin**: Custom red vertical slider skin for volume control
- **RXMediaProgressBar**: Media progress bar providing intuitive playback progress display
- **SvgIcon**: Custom control supporting SVG icons with dynamic path modification

### 3. Style Management
- **CSS Style Sheets**: External CSS files for managing interface styles
- **Theme Color Scheme**: Chinese red as the main color scheme to create a music playback atmosphere
- **Responsive Design**: Adaptive layout supporting different screen sizes

## Project Features

### 1. User Experience Optimization
- **Smooth Interaction**: Fast interface response and natural operation flow
- **Visual Feedback**: Rich visual feedback such as hover effects and selection states
- **Quick Operations**: Supports multiple interaction methods including mouse clicks and dragging

### 2. Code Quality
- **Modular Design**: Separated functional modules with clear code structure
- **Object-Oriented**: Follows object-oriented design principles for easy maintenance and extension
- **Exception Handling**: Complete exception handling mechanism to improve application stability

### 3. Extensibility
- **Plugin Architecture**: Supports flexible extension of functional modules
- **Configuration Management**: Application parameters managed through configuration files
- **Internationalization Support**: Reserved internationalization interfaces supporting multiple languages

## Usage Instructions

### System Requirements
- Java 8 or higher version
- Operating system supporting JavaFX (Windows, macOS, Linux)

### Running the Application
1. Ensure Java runtime environment is installed on the system
2. Compile the project source code
3. Run the App main class to start the application

### Function Operations
- Click left menu items to switch between different functional modules
- Use the bottom playback control bar for music playback control
- Click the volume button to adjust playback volume
- Drag the progress bar to control playback position

## Project Structure

```
src/main/
├── java/com/stickpoint/ddmusic/
│   ├── App.java              # Application entry point
│   ├── common/               # Common utility classes
│   ├── page/                 # Page components
│   │   ├── node/             # UI components
│   │   └── skin/             # Custom skins
│   └── utils/                # Utility classes
└── resources/
    ├── css/                  # Style files
    ├── font/                 # Font files
    ├── img/                  # Image resources
    └── media/                # Audio files
```


## Development Features

The DdMusic project demonstrates best practices for modern JavaFX application development:
- Adoption of MVC architecture pattern separating business logic from interface presentation
- Custom controls and skins implementing unique visual effects
- Complete resource management avoiding memory leaks
- Elegant exception handling mechanism enhancing user experience

The project code is规范 (standardized) with complete comments, making it easy for developers to understand and extend. Through reasonable package structure design and class responsibility division, the code maintainability and extensibility are ensured.

## Future Prospects

DdMusic, as a basic music player framework, has good extensibility and can add the following features in the future:
- Online music playback support
- Lyrics synchronization display
- Audio effect adjustment functions
- Customizable playlists
- Theme switching functionality

This project provides a valuable reference implementation for JavaFX desktop application development.