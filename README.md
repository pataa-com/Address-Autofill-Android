# To add Pataa autofill address view on your app please follow the instructions.

![image](https://user-images.githubusercontent.com/103625941/163770534-6cec5e08-00c1-48f5-9ab8-4db4027aa820.png)

![image](https://user-images.githubusercontent.com/103625941/164167880-9334e305-62e1-47b1-86c2-1e336a40e481.png)

![image](https://user-images.githubusercontent.com/103625941/164168176-cdf9148e-98e3-4dd4-8bb7-e19b1400ef8e.png)


**Step 1. Add it in your root build.gradle/settings.gradle at the end of repositories**

**In root build.gradle**


	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	

**OR**
	
**In settings.gradle**


	dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

**Step 2. Add the dependency**

	dependencies {
	        implementation 'com.github.pataa-com:Address-Autofill-Android:v1.0.4'
	}


**Step 3. Add Metadata in manifest**


        <meta-data
            android:name="pataa.autofill.sdk.ClientKey"
            android:value="@string/PATAA_API_KEY" />

        <meta-data
            android:name="pataa.autofill.sdk.EnableLogger"
            android:value="true" />



**Step 4. Add view on xml(Don't change the id)**


        <com.pataa.sdk.PataaAutoFillView
        android:id="@+id/vPataaCodeView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />



**Step 5. Set properties and listener(Only for JAVA)**, For kotlin skip this step


        ((PataaAutoFillView) findViewById(R.id.vPataaCodeView))
       .setCurrentActivity(this)//to get the result of create pataa
       .setAddressCallBack(new OnAddress() {//to get the click events
           @Override
           public void onNetworkIsNotAvailable() {
               Toast.makeText(MainActivity.this, "onNetworkIsNotAvailable", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onPataaNotFound(String message) {

           }


           @Override
           public void onPataaFound(User user, Pataa
                   response) {
               Toast.makeText(MainActivity.this, user.getFirst_name() + " : " + response.getFormattedAddress(), Toast.LENGTH_SHORT).show();
           }
       });


**Step 6. Set properties and listener(Only for Kotlin)**, For Java skip this step


        findViewById<PataaAutoFillView>(R.id.vPataaCodeView)
            .setCurrentActivity(this)
            .setAddressCallBack(object : OnAddress {
                override fun onNetworkIsNotAvailable() {
                    Toast.makeText(applicationContext, "onNetworkIsNotAvailable", Toast.LENGTH_SHORT).show();
                }

                override fun onPataaNotFound(message: String?) {

                }

                override fun onPataaFound(user: User?, response: Pataa?) {
                    Toast.makeText(applicationContext, user?.getFirst_name() + " : " + response?.getFormattedAddress(), Toast.LENGTH_SHORT).show();
                }
            })



**Step 7. Add PATAA api key on string.xml**


        <resources>
    	.....
		<string name="PATAA_API_KEY">+ipXPwNxxxxxxxxxxxxxxxxxxxxxxxxxxxxxvkk=</string>
        <resources>

***
**In case of errors**

        E/PATAA_SDK_LOGS: {"msg":"Invalid App key","status":600}
        E/PATAA_SDK_LOGS: Invalid App key
        
**Solution** : Enable logs from manifest metadata and copy the SHA1 key. Put it on pataa developer console, and then try again you will got the results

***
        E/PATAA_SDK_LOGS: {"msg":"Your key is deavtivated please generate new","status":200}
        E/PATAA_SDK_LOGS: Invalid App key
        
**Solution** : Enable the key for use on Pataa developer console or Create new key.

***
        E/PATAA_SDK_LOGS: {"msg":"Pataa Code not found","status":204}
        E/PATAA_SDK_LOGS: Pataa Code not found
        
**Solution** : Try to search with valid pataa code like - KUMAR100, SINGH221, Because your searched pataa code is not created on pataa platform yet.

***        
**For any other error** : [Please refer the document](https://docs.pataa.com/refrence/#error-handling) 










