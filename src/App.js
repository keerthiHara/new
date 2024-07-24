import React, { useEffect } from 'react';
import {
  View,
  Button,
  StyleSheet,
  PermissionsAndroid,
  Platform,
  Alert,
  Text,
} from 'react-native';
import CallRecorder from './CallRecorder';  // Assuming CallRecorder is your module

const App = () => {
  useEffect(() => {
    requestPermissions();
  }, []);

  const requestPermissions = async () => {
    if (Platform.OS === 'android') {
      try {
        const permissions = [
          PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
          PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
          PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
          PermissionsAndroid.PERMISSIONS.READ_PHONE_STATE,
          PermissionsAndroid.PERMISSIONS.PROCESS_OUTGOING_CALLS,
        ];

        // Add MANAGE_EXTERNAL_STORAGE for Android 30 and above
        if (Platform.Version >= 30) {
          permissions.push(PermissionsAndroid.PERMISSIONS.MANAGE_EXTERNAL_STORAGE);
        }

        const granted = await PermissionsAndroid.requestMultiple(permissions);

        // Log granted permissions for debugging
        console.log('Granted permissions:', granted);

        // Check if all permissions are granted
        const allPermissionsGranted = Object.values(granted).every(
          permission => permission === PermissionsAndroid.RESULTS.GRANTED
        );

        if (allPermissionsGranted) {
          console.log('All permissions granted');
        } else {
          Alert.alert('Permission denied', 'All permissions are required for this app to work properly.');
        }
      } catch (err) {
        console.warn(err);
      }
    }
  };

  const startRecording = () => {
    try {
      CallRecorder.startRecording();
    } catch (error) {
      console.error("Error starting recording:", error);
    }
  };
  
  const stopRecording = () => {
    try {
      CallRecorder.stopRecording();
    } catch (error) {
      console.error("Error stopping recording:", error);
    }
  };
  
  const checkIsRecording = () => {
    try {
      CallRecorder.isRecording((isRecording) => {
        console.log('Is recording:', isRecording);
      });
    } catch (error) {
      console.error("Error checking recording status:", error);
    }
  };
  
  return (
    <View style={styles.container}>
    <Text>welocmr</Text>
      <Button title="Start Recording" onPress={startRecording} />
      <Button title="Stop Recording" onPress={stopRecording} />
      <Button title="Check if Recording" onPress={checkIsRecording} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default App;
