# 🧩 User Provisioning with Keycloak

## 🧩 Ways Users Exist in Keycloak

### ✅ 1. Self-Registration (Signup in Keycloak)

- Enable signup page in Keycloak
- User creates:
    - username
    - password
- Then logs in

👉 Common for:
- Public applications
- SaaS platforms

---

### ✅ 2. Admin-Created Users

- Admin creates users via:
    - Admin panel
    - API

👉 Common for:
- Enterprise/internal systems
- School/company platforms

---

### ✅ 3. External Login (No Manual Registration) ⭐

- User logs in via:
    - Google
    - Facebook
    - Company SSO

👉 Keycloak automatically:
- Creates the user internally
- Stores their identity

✔ User never manually registers  
✔ But still ends up in Keycloak

---

## 🔄 Full Authentication + Provisioning Flow

1. User logs in via Keycloak  
   (username/password OR Google/SSO)

2. If user does not exist in Keycloak:
    - Keycloak may create them automatically (SSO/federation)

3. Keycloak authenticates user

4. Your application receives JWT token

5. Your application:
    - Checks database
    - Creates user (JIT provisioning) if not exists

---

## 🎯 Key Idea (Very Important)

There are **TWO separate user systems**:

### 1. Keycloak (Identity Provider)
- Handles authentication
- Stores credentials
- Identifies the user

### 2. Your Application Database
- Stores application-specific data:
    - Orders
    - Profiles
    - Preferences
- Users created via:
    - JIT provisioning (e.g., filter/service)

---

## 🔥 Real-World Analogy

- **Keycloak = Passport Office**
- **Your App = Hotel**

👉 User must have a passport (Keycloak account)  
👉 When they visit the hotel → you create a record

---

## ⚠️ Important Clarification

- Your system does **NOT replace registration**
- It avoids duplicating user creation in your app database

---

## ✅ Summary

✔ Users must exist in Keycloak before login  
✔ They don’t always manually register  
✔ Your filter/service handles app-level user creation (not authentication)

---

## 🧠 Industry practice

👉 Most experienced developers:

❌ DO NOT use @Builder on:
- Entities (User, Order, etc.)

✅ USE @Builder on:
- DTOs
- Request/Response objects

---
## 🔥 Simple rule

👉 If browser auto-sends credentials → ENABLE CSRF
👉 If client manually sends token → DISABLE CSRF