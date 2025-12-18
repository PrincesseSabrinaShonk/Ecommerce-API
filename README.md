
<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/4404b6c8-fff2-48a3-b6e4-b4fb1caa7c08" />
<h1 align="center"> EasyShop – E-Commerce REST API</h1>

<p align="center">
  A fully functional backend API for an e-commerce platform
</p>

<hr/>

<h2> Project Overview</h2>

<p>
Easy Shop is a full-stack e-commerce API that allows users to browse products, manage shopping carts, and place orders,
  while giving administrators secure tools to manage products and categories. The application is built with Spring Boot and follows RESTful design principles,
  including proper HTTP methods, status codes, and role-based access control. Security is enforced using authentication and authorization to ensure only admins can perform sensitive actions like creating,
  updating, or deleting products. Throughout the project, I focused on fixing bugs, improving endpoint behavior,
  and validating functionality through testing to ensure the application works reliably and securely.
</p>

<p>
</p>

<hr/>

<h2>🛠️ Technologies Used</h2>

<ul>
  <li><strong>Java</strong></li>
  <li><strong>Spring Boot</strong></li>
  <li><strong>Spring Security (JWT Authentication)</strong></li>
  <li><strong>MySQL</strong></li>
  <li><strong>JDBC / DAO Pattern</strong></li>
  <li><strong>Insomnia</strong> (API testing)</li>
</ul>

<hr/>

<h2>🔐 Authentication & Authorization</h2>

<p>
The application uses <strong>JWT-based authentication</strong> to secure endpoints.
Users must log in to receive a token, which is then required to access protected routes.
</p>

<ul>
  <li><strong>ROLE_USER</strong> – Can browse products, manage shopping cart, and place orders</li>
  <li><strong>ROLE_ADMIN</strong> – Can create, update, and delete categories and products</li>
 <img width="1118" height="463" alt="image" src="https://github.com/user-attachments/assets/d63f9a7c-7c67-4e88-8644-79db33c12882" />
<img width="1029" height="422" alt="image" src="https://github.com/user-attachments/assets/edd9788c-a655-4feb-a8a4-9a5a2d779b55" />
<img width="998" height="305" alt="image" src="https://github.com/user-attachments/assets/69cd28f3-0512-415b-9e03-7e93d962a169" />





</ul>

<hr/>

<h2>📂 Database Design</h2>

<p>
The database is designed using a relational structure and includes the following tables:
</p>

<ul>
  <li>users</li>
  <li>profiles</li>
  <li>categories</li>
  <li>products</li>
  <li>shopping_cart</li>
  <li>orders</li>
  <li>order_line_items</li>
</ul>

<p>
Foreign keys are used to enforce data integrity between users, orders, products,
and categories.
</p>

<hr/>

<h2>📦 Features Implemented</h2>

<h3>🗂️ Categories</h3>
<ul>
  <li>View all categories</li>
  <li>View category by ID</li>
  <li>Create a new category (Admin only)</li>
  <li>Update an existing category (Admin only)</li>
  <li>Delete a category (Admin only)</li>
</ul>

<h3>🛍️ Products</h3>
<ul>
  <li>View all products</li>
  <li>View product by ID</li>
  <li>Filter products by category</li>
  <li>Create, update, and delete products (Admin only)</li>
</ul>

<h3>🛒 Shopping Cart</h3>
<ul>
  <li>Add products to cart</li>
  <li>Update product quantities</li>
  <li>View cart items</li>
  <li>Remove individual products from cart</li>
  <li>Clear entire cart</li>
</ul>

<h3>📦 Orders</h3>
<ul>
  <li>Create an order from shopping cart</li>
  <li>Persist order details and line items</li>
  <li>Associate orders with authenticated users</li>
</ul>

<hr/>

<h2>🔎 API Testing</h2>

<p>
All endpoints were tested using <strong>Insomnia</strong> to ensure proper request handling,
authentication enforcement, and correct HTTP status codes.
</p>

<p>
Error handling was implemented using appropriate responses such as:
</p>

<ul>
  <li>200 OK</li>
  <li>201 Created</li>
  <li>204 No Content</li>
  <li>401 Unauthorized</li>
  <li>403 Forbidden</li>
  <li>404 Not Found</li>
</ul>

<hr/>

<hr/>

<h2>✨ Interesting Piece of Code </h2>

<p>
One piece of code I am particularly proud of is the user registration endpoint. This method is responsible for creating new users while enforcing validation,
  security best practices, and clean error handling.

It normalizes usernames to ensure consistency across authentication,
verifies that passwords match, prevents duplicate account creation, and securely persists both the user and their associated profile. 
Additionally, it uses meaningful HTTP status codes and exception handling to provide clear feedback for both client errors and unexpected server issues.
</p>

<img width="1152" height="766" alt="image" src="https://github.com/user-attachments/assets/03926017-5407-486b-b9a2-3e7eb6cba4c0" />

<hr />

<h2 style="color:#38bdf8; text-shadow: 0 0 6px rgba(56,189,248,0.6);">
What This Project Taught Me
</h2>

<p style="color:#e5e7eb; font-size:15px; line-height:1.6;">
Building <strong style="color:#22d3ee;">EasyShop</strong> pushed me to think beyond individual API endpoints and focus on how a complete backend system works together. I learned how to design a REST API that feels realistic and production-ready, where <span style="color:#a78bfa;">security</span>, <span style="color:#34d399;">data integrity</span>, and <span style="color:#facc15;">user experience</span> all matter.
</p>

<p style="color:#e5e7eb; font-size:15px; line-height:1.6;">
This project strengthened my understanding of how <strong style="color:#22d3ee;">Spring Boot</strong> applications are structured, how authentication and authorization work in a real-world context using <strong style="color:#f472b6;">JWTs</strong>, and how role-based access control directly impacts API design and testing.
</p>

<p style="color:#e5e7eb; font-size:15px; line-height:1.6;">
I also gained hands-on experience designing relational databases, enforcing relationships with foreign keys, and implementing the <strong style="color:#34d399;">DAO pattern</strong> to keep data access clean, maintainable, and scalable.
</p>

<p style="color:#e5e7eb; font-size:15px; line-height:1.6;">
Most importantly, this project taught me how to debug and reason through backend issues by tracing requests across controllers, security filters, and the database. It significantly improved my confidence in building, securing, and testing a full-featured <strong style="color:#22d3ee;">Java backend application</strong> from start to finish.
</p>

<div style="
  margin-top:20px;
  padding:14px;
  border-left:4px solid #38bdf8;
  background-color:#0f172a;
  color:#c7d2fe;
  font-size:14px;
">
🚀 <strong>Outcome:</strong> This capstone challenged me to think like a backend engineer working on a real production system, not just a school assignment.
</div>
<img width="1024" height="1024" alt="image" src="https://github.com/user-attachments/assets/5168dc8a-3bac-4f04-8249-ac4285eb57f1" />


<hr />





