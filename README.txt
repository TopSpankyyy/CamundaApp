To run app:
-Open Camunda_jar
-Run Camunda.jar application
-Click 'Grab new photo' to retrieve a new photo from the Bear API
-Click 'Save' to save the current URL to the DB
-Click 'Load latest save' to reload the most recently saved photo
-'Test' can be clicked, but will not update UI or display logs unless debugging in IDE. Can be used to verify functions work properly


Info on app:
-Code can be found at Camunda/src/main/java/org/example
-Main.java controls JFrame UI. Builds frame, distinguishes/routes actions, updates photo display
-PhotoGrab.java provides the functionality behind the app. Includes the functions that are called on button press for 'Grab new photo', 'Save', 'Load latest save'
-UnitTests.java provides testing for each function in PhotoGrab.java