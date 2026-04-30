import { createContext } from 'react';

export const AuthContext = createContext(null);

// Context shape:
// {
//   token:           string | null,
//   isAuthenticated: boolean,
//   isRecruiter:     boolean,
//   loginUser:       (token: string) => void,
//   logoutUser:      () => Promise<void>,
// }