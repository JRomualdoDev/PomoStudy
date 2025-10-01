import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface AuthenticationDTO {
  email: string;
  password: string;
}

export interface UserCreateRequestDTO {
  name: string;
  email: string;
  password: string;
}

export interface LoginResponseDTO {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = '/api/auth';

  constructor(private http: HttpClient) { }

  login(credentials: AuthenticationDTO): Observable<LoginResponseDTO> {
    return this.http.post<LoginResponseDTO>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        const decodedToken = this.decodeToken(response.token);
        console.log('Decoded Token Payload:', decodedToken);
        localStorage.setItem('token', response.token);
        if (decodedToken && decodedToken.userId) {
          localStorage.setItem('userId', decodedToken.userId);
        }
      })
    );
  }

  register(userInfo: UserCreateRequestDTO): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userInfo);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
  }

  getUserId(): string | null {
    return localStorage.getItem('userId');
  }

  private decodeToken(token: string): any {
    try {
      return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
      return null;
    }
  }
}
