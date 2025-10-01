import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from './auth.service';

export interface Category {
  id: number;
  name: string;
  color: string;
  icon: string;
}

export interface CategoryRequestDTO {
  name: string;
  color: string;
  icon: string;
  userId: number;
}

export interface Page<T> {
  content: T[];
  // Add other pagination properties if needed
}

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private apiUrl = '/api/category';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getCategories(): Observable<Category[]> {
    return this.http.get<Page<Category>>(this.apiUrl).pipe(
      map(response => response.content)
    );
  }

  createCategory(category: Omit<CategoryRequestDTO, 'userId'>): Observable<Category> {
    const userId = this.authService.getUserId();
    if (!userId) {
      return throwError(() => new Error('User ID not found. Please log in again.'));
    }
    const categoryRequest: CategoryRequestDTO = {
      ...category,
      userId: +userId
    };
    return this.http.post<Category>(this.apiUrl, categoryRequest);
  }

  updateCategory(id: number, category: CategoryRequestDTO): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/${id}`, category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
