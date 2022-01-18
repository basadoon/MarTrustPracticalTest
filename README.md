# MarTrustPracticalTest
MarTrust Practical Test Submission

# How to run?
After cloning the repo, if you have eclipse IDE, import Existing Maven Projects in file tab in the upper left corner. After that, go to src/main/java 
where you will see the com.basaron.fxrates package. Right click on FxRateWebserviceApplication.java file and Run As Java Application.


# How to test endpoints?
You can manually test it in postman, but I suggest accessing this instead http://localhost:8080/swagger-ui.html (This is accessible after running FxRateWebserviceApplication.java)
. Here is a link for the swagger documentation where the endpoints can easily be tested.


# How to run unit tests?
The unit tests are in src/test/java. It can be run by right clicking at the unit test file and finding run as, and under run as there is a Run configurations button.
After clicking the Run configurations button, change the Test Runner to JUnit 4 under Test Tab in run configurations. This needs to be done
with every test files. Click apply, Then click run.
