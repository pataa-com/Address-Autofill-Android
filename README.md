# To add Pataa autofill address view on your app please follow the instructions.

![image](https://user-images.githubusercontent.com/103625941/163770534-6cec5e08-00c1-48f5-9ab8-4db4027aa820.png)


**Step 1. Add it in your root build.gradle at the end of repositories:**

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

**Step 2. Add the dependency**

	dependencies {
	        implementation 'com.github.pataa-com:Address-Autofill-Android:Tag'
	}


**Step 3. Add Metadata in manifest**


        <meta-data
            android:name="pataa.autofill.sdk.ClientKey"
            android:value="@string/PATAA_API_KEY_TESTING" />

        <meta-data
            android:name="pataa.autofill.sdk.EnableLogger"
            android:value="true" />



**Step 4. Add view on xml**


        <com.pataa.sdk.PataaAutoFillView
        android:id="@+id/vPataaCodeView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />



**Step 5. Create an object in activity**


        PataaAutoFillView pataaAddress;


**Step 6. Initialize the object**


        pataaAddress = findViewById(com.pataa.sdk.R.id.vPataaCodeView);


**Step 7. Set properties and listener**


        pataaAddress
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
           public void onPataaFound(GetPataaDetailResponse.User user, GetPataaDetailResponse.Pataa
                   response) {
               Toast.makeText(MainActivity.this, user.getFirst_name() + " : " + response.getFormattedAddress(), Toast.LENGTH_SHORT).show();
           }
       });


**Step 8. Add on activity result**


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_KEY_CREATE_PATAA && data != null && data.hasExtra(ON_ACT_RSLT_PATAA_DATA)) {
            String pc = data.getStringExtra(ON_ACT_RSLT_PATAA_DATA);
            if (pataaAddress != null) {
                pataaAddress.getPataadetail(pc);
            }
        }
    }


