# InventarisHub
InventarisHub is an item management application that makes it easy for users to manage inventory with the following features :
* Input item data efficiently to monitor stock
* View historical item data to make finding information related to item usage easier.
* Displays a detailed list of items, including stock information, prices, and item categories.

# Preview

<div style="display: flex; gap: 10px; flex-wrap: wrap;">

<img src="https://github.com/xryar/assets/blob/master/InventarisHub/login.png"  width="250" height="500">
<img src="https://github.com/xryar/assets/blob/master/InventarisHub/register.png"  width="250" height="500">
<img src="https://github.com/xryar/assets/blob/master/InventarisHub/home.png"  width="250" height="500">
<img src="https://github.com/xryar/assets/blob/master/InventarisHub/history.png"  width="250" height="500">
<img src="https://github.com/xryar/assets/blob/master/InventarisHub/upload.png"  width="250" height="500">
<img src="https://github.com/xryar/assets/blob/master/InventarisHub/detail.png"  width="250" height="500">
<img src="https://github.com/xryar/assets/blob/master/InventarisHub/profile.png"  width="250" height="500">

</div>

# Libraries and technologies used
- Navigation component : one activity contains multiple fragments instead of creating multiple activities.
- Retrofit : making HTTP connection with the rest API and convert json file to Kotlin/Java object.
- MVVM & LiveData : Saperate logic code from views and save the state in case the screen configuration changes.
- Coroutines : do some code in the background.
- view binding : instead of inflating views manually view binding will take care of that.
- Glide : Catch images and load them in imageView.
