Implement Simple Calculator Android App by Reusing Logics in Rust via JavaScript-WASM Interfacing

Reference:
* https://github.com/rustwasm/wasm-bindgen/tree/main/examples/without-a-bundler


Steps:

* create android studio project
* try build and run the app
* create repository
* open project folder with VSCode
* add folder `rust`
* add `rust\Cargo.toml`
* add folder `rust\src\lib`
* add rust `rust\index.html`
* add `rust\build.sh`
* after running `build.sh` will product content in
  - `rust\target` -- cargo build result
  - `rust\pkg` -- WASM output
* start ***Live Server*** VSCode extension
  - visit localhost:5501
  - navigate to `rust` folder
  - click the `call WASM` button
* or use Python's `http.server`
  - visit localhost:8000


Android side:
* ensure folder `app/src/main/assets/bridge`
* permissions:
  - access Internet:
    ```
    <uses-permission android:name="android.permission.INTERNET" />
    ```
  - `WebView` allow *clear text" traffic  
    ```
    android:usesCleartextTraffic="true"
    ```  
* If use real phone, assume it connects to your local network
* add `MainView`
  - for `WebViewAssetLoader` will need to include some additional .. just let Android Studio include it


Trying app out:
* Suggest to use Python's `http.server`, or else might get into *firewall issue*
* Need to find out the IP of this development machine
  - set `REMOTE_URL` in `MainActivity.kt`  according to your development machine's IP
* Deploy the app  


Others
* Preview
* Fix Orientation
* 