# Voice Recognition application using Java

The Application is a basically command line application.
The audio is taken as an amplitude byte array and then the get subjected to some basic noise reduction functions.
Then app splits the filtered array by the length of the word spoken when recording. this removes the unnecessary parts from the recorded audio. Basic mathematical operations are involved in here to differentiate noise with voice.
then app plots the graph of the audio and compare it with original audio for their similarity using basic image processing techniques.
To increase the accuracy, when adding password it takes 5 audio samples from user.
Login details are encrypted and for decryption password should be entered by the user. 
To increase the security, similarity of the original word sample and login audio should be greater than 60% and the time taken to type the encryption password should match.
No any external libraries or frameworks are used.
**.Same word that was used during the adding voice password should be used when log in. Tested in different environments. Quite environments give better responds for log in 
