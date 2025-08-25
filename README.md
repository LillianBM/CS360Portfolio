# CS360Portfolio
1. The app I developed is an inventory management application designed to help users track update, and organize their items efficently. The primary user need this app addresses is the ability to manage physical or digital items by storing essential details such as item name, quantity, and description in a persistent database. It also allows users to receive sms alerts based on specific triggers, such as low inventory. The goal was to create a user-friendly app that supports essential inventory operations in an mobile enviormnet.
2. The core screens and features included: Login/sing-Up screen: for user authentication and secure access. Inventory screen: where users can view, add, update and delete inventory items. Add/Update dialogs: modal dialogs for intitutive item entry and editing. Logout button for clear session control. SMS permission trigger: to request and handle permission for text alerts.
3.  approached the coding process in modular steps, starting with building the user authentication system, followed by integrating the SQLite database for persistent storage, and then layering on UI interactions like adding, updating, and deleting items. I used helper classes to separate database logic from the UI, kept methods short and focused, and added inline comments for clarity.

A technique that helped me was to test after each new feature, so I could isolate bugs early. Also, I constantly referred to the emulator output and error logs for debugging. These strategies are universally useful and can be applied to any future app development projects, especially those requiring multiple interacting components.

⸻

4. How did you test to ensure your code was functional? Why is this process important, and what did it reveal?

I used the Android Emulator to test the app across multiple scenarios:
	•	Creating a new user
	•	Logging in with existing credentials
	•	Adding new inventory items
	•	Updating and deleting items
	•	Testing SMS functionality with permissions granted and denied

This testing process is crucial because it reveals runtime issues like crashes, UI misalignment, or logic bugs that aren’t caught by syntax checks. It helped me discover things like missing constraints in XML layouts or incorrect permission declarations in the Manifest file.

⸻

5. Consider the full app design and development process from initial planning to finalization. Where did you have to innovate to overcome a challenge?

One area where I had to innovate was in managing the dynamic layout for inventory items. Initially, I had difficulty displaying the description field properly—it was cut off or unreadable. I solved this by rethinking the layout using LinearLayout and adjusting padding, margins, and layout weights. I also had to carefully manage runtime permissions for SMS, ensuring that the app didn’t crash if permissions were denied. These required out-of-the-box thinking and real-time problem-solving.

⸻

6. In what specific component of your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?

I was particularly successful in building the SQLite database integration with CRUD operations. My app can seamlessly add, read, update, and delete inventory entries while maintaining persistent storage. This demonstrated my understanding of structured database design, content display in UI components, and interaction between Java code and XML layouts. It also showcased my ability to link user actions to backend logic in a clean, maintainable way.



