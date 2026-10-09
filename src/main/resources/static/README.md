# Crumb Bakery Frontend

## Run
1. Extract this ZIP.
2. Open index.html in a modern browser, or open the folder in VS Code and use Live Server.
No npm install or build step is needed.

## Sign in and sign up
Click the single profile icon in the header to open the Sign in / Sign up popup. Once signed in, the same icon opens profile and sign-out options. For the initial sample profile, use alex@example.com and any made-up password of at least 6 characters. Sign up replaces the sample profile for this demo session. Passwords are not stored or verified; password confirmation validates only the form. Sign out returns to guest mode. This is not secure authentication or access control.

The header, account forms and selected panels now use subtle glass surfaces with light/dark variants and opaque fallbacks when blur is unsupported.

## Updated UI
- Light/dark toggle in the header. Defaults to your system preference and remembers your selection on this browser.
- Responsive rounded cards, translucent navigation, refined typography and warm accent colours.
- Hover effects, a single smooth page transition, button ripples and cart feedback.
- Respects the operating system reduced-motion preference.

## Files
- index.html: page shell, navigation, footer, dialog and notifications.
- style.css: responsive styles, colours, typography and layouts.
- theme.js: colour preference and UI motion effects.
- app.js: product catalogue, cart, cake requests, bookings, customer profile, orders, reviews and admin interactions.
- Image files: keep alongside index.html so relative image paths work.

## Navigation
The app uses hash navigation: #shop, #cakes, #cart, #orders, #bookings, #account, #reviews, #contact and #admin.
Use the Admin workspace link in the footer to manage demo records.

## Scope
This is a frontend demonstration for SE1020. Data lives in JavaScript memory and resets on refresh. Sign-in and payments are simulated. Do not use real passwords or payment details.
For your assignment, connect the frontend to your Java backend and its required text-file storage. Authentication, authorization, server-side validation, and payment processing are not implemented here.
Google Fonts requires internet access; fallback fonts work offline.

## Image sources
croissant.jpg: https://unsplash.com/photos/a-croissant-on-a-plate-with-butter-and-a-knife-fc8y5qpBKWQ
chocolate-cake.webp: https://www.anchorcafeandroastery.com/bakery
artisan-bread.webp: https://maingrainbakery.com/sourdough-starter
The cake and bread photos are reference imagery from bakery websites; reuse licensing was not established. Replace them with your own or licensed photos before public/commercial reuse.

## Contact page
Contact us replaces My orders in the main navigation. Order history remains available through the signed-in profile popup and the Contact us page. The enquiry form validates and previews a message; it does not send or store enquiries. Connect a backend contact service before using it for real enquiries.

## Smooth navigation
Nested entrance fades and delayed scroll reveals were removed to prevent flickering. Supported browsers use one 180 ms View Transition with a steady header. Other browsers use a short movement without fading the content out. Route scroll position resets immediately, the scrollbar gutter stays stable, and reduced-motion preferences disable page animation.

## Navigation indicator
The header uses one persistent yellow indicator under the active tab. A normal click moves that same line to the clicked tab with a 340 ms slide and updates its width for the label. Middle click and modifier clicks preserve normal browser link behavior.

## Navigation and floating actions
Reviews is now in the main navigation and Contact us is last. A persistent WhatsApp button opens a prefilled message shortcut through WhatsApp. Replace its `wa.me/?text=...` href in `index.html` with your bakery’s WhatsApp number when you have one. The back-to-top arrow appears after scrolling and uses smooth scrolling unless reduced motion is enabled.

## Owner workspace
Open **Admin workspace** in the footer. The owner demo has tabs for Products, Users, Custom cakes, Orders, Bookings, Payments and Reviews. Products and users support add, edit and delete. Orders support editing, status changes, one-click Complete, cancellation and owner feedback. Completing an order unlocks the customer review flow; feedback appears in the customer’s order details.

The current workspace is frontend-only. Connect it to Java authentication, authorization and the required text files before treating the owner actions as secure or persistent.

## Role-based views
Guests and customer accounts stay on the public storefront. The owner demo signs in through the same profile icon and is redirected to a private admin shell; the public navigation and footer are hidden there. Opening `#admin` as a guest or customer redirects to the storefront.

Prototype owner login: `admin@crumb.lk` / `admin123`. Replace this frontend-only demo credential with Java authentication, sessions and role checks before deployment. The backend must protect every admin endpoint; hiding the route in the browser is only presentation.

## Inventory and sold-out products
The owner product editor includes quantity in stock and an Available/Sold out option. Customers see Available, “N left”, or Sold out. Sold-out products cannot be added to the bag, cart quantities cannot exceed stock, orders reduce stock, and cancelled orders restore stock unless the owner marked the product manually sold out.

Replace the in-memory inventory with Java/file-backed data for the assignment backend.
