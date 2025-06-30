# ProfilePicture Component

A reusable React component for displaying user profile pictures with automatic caching, fallback support, and real-time updates.

## Features

- **Automatic Cache Management**: Uses browser caching with cache-busting when pictures are updated
- **Fallback Support**: Automatically falls back to a default avatar if the profile picture fails to load
- **Real-time Updates**: Listens for profile picture update events and refreshes automatically
- **Loading States**: Provides loading state classes for styling during image load
- **Custom Error Handling**: Supports custom onLoad and onError callbacks

## Usage

### Basic Usage

```jsx
import { ProfilePicture } from 'components/common/ProfilePicture';

// Simple profile picture
<ProfilePicture userId={user.id} />

// With custom styling
<ProfilePicture
  userId={user.id}
  className="rounded-circle"
  style={{ width: 50, height: 50 }}
  alt={`${user.firstName} ${user.lastName}`}
/>
```

### In Headers/Navigation

```jsx
<ProfilePicture
  userId={user?.id}
  alt={t('common.userAvatar')}
  className="user-avatar"
/>
```

### In Profile Pages

```jsx
<ProfilePicture
  userId={user.id}
  alt={`${user.firstName} ${user.lastName}`}
  className="rounded-circle mb-3"
  style={{ width: 120, height: 120, objectFit: 'cover' }}
/>
```

### With Custom Error Handling

```jsx
<ProfilePicture
  userId={user.id}
  onLoad={() => console.log('Profile picture loaded')}
  onError={e => console.log('Failed to load profile picture', e)}
  fallbackSrc="/custom-default-avatar.png"
/>
```

## Props

| Prop          | Type             | Default                 | Description                                            |
| ------------- | ---------------- | ----------------------- | ------------------------------------------------------ |
| `userId`      | `string\|number` | -                       | The ID of the user whose profile picture to display    |
| `className`   | `string`         | `''`                    | CSS classes to apply to the image                      |
| `alt`         | `string`         | `'Profile Picture'`     | Alt text for the image                                 |
| `fallbackSrc` | `string`         | `'/default-avatar.png'` | Fallback image path when profile picture fails to load |
| `style`       | `object`         | `{}`                    | Inline styles to apply to the image                    |
| `onLoad`      | `function`       | `null`                  | Callback fired when image loads successfully           |
| `onError`     | `function`       | `null`                  | Callback fired when image fails to load                |

## Events

The component listens for `profilePictureUpdated` events on the window object. When a profile picture is updated, fire this event to refresh all ProfilePicture components for that user:

```javascript
// Trigger update for user ID 123
window.dispatchEvent(
  new CustomEvent('profilePictureUpdated', {
    detail: { userId: 123 },
  })
);
```

## Integration with Services

The component automatically integrates with your user service. When uploading or resetting profile pictures, the service will automatically trigger update events:

```javascript
// Upload new profile picture
await userService.uploadProfilePicture(userId, file);
// ProfilePicture components will automatically refresh

// Reset to default
await userService.resetProfilePicture(userId);
// ProfilePicture components will automatically refresh
```

## Utility Functions

Use the utility functions for additional profile picture management:

```javascript
import {
  triggerProfilePictureUpdate,
  preloadProfilePicture,
  getProfilePictureUrl,
} from 'utils/profilePictureUtils';

// Manually trigger an update
triggerProfilePictureUpdate(userId);

// Preload a profile picture for better UX
await preloadProfilePicture(userId);

// Get the URL for a profile picture
const url = getProfilePictureUrl(userId);
```

## CSS Classes

The component applies the following CSS classes:

- Base classes: Whatever you pass in `className`
- Loading state: `loading` class is added during image load
- You can style these in your CSS:

```css
.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.user-avatar.loading {
  opacity: 0.7;
  /* Add loading animation if desired */
}
```

## Backend Requirements

Ensure your backend serves profile pictures at `/api/users/{userId}/profile/picture` with proper cache headers:

```java
return Response.ok(imageData)
    .header("Content-Type", contentType)
    .header("Cache-Control", "public, max-age=86400, immutable")
    .header("Content-Disposition", "inline; filename=\"" + pictureName + "\"")
    .build();
```
