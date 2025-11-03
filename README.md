🏬 StoreApp — Spring Boot CRUD + Image Upload

📦 A basic product management app using Spring Boot, Thymeleaf, MySQL
🖼 Supports local image upload, update, and delete

✅ Features

📃 Product listing

➕ Add product

✏️ Edit product

🔁 Update image

❌ Delete product + image

✅ Validation

🎨 Bootstrap UI

🛠 Tech Stack
🔹	Tech
☕	Spring Boot
🎨	Thymeleaf
🗄️	MySQL
🧱	JPA + Hibernate
💾	Local file storage
🔧	Maven

📂 Project Structure
📁 StoreApp
 ├─ 📁 controller
 ├─ 📁 dto
 ├─ 📁 entity
 ├─ 📁 repository
 ├─ 📁 templates/products
 └─ 📁 public/images   ← image storage

🔗 API Endpoints
Method	Route	🔎
GET	/products	📃 list
GET	/products/create	➕ form
POST	/products/create	✅ save
GET	/products/edit?id={id}	✏️ form
POST	/products/edit	🔁 update
GET	/products/delete?id={id}	❌ delete

<img width="1890" height="800" alt="image" src="https://github.com/user-attachments/assets/adf2ae00-f737-4b16-ab1f-27178922b018" />


<img width="1856" height="802" alt="image" src="https://github.com/user-attachments/assets/fc36c439-5886-4faa-a26d-bb485c19a52a" />

