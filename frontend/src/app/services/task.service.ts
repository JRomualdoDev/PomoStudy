import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Page } from './category.service';

export enum StatusUser {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELED = 'CANCELED'
}

export enum TaskPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH'
}

export interface Task {
  id: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  status: StatusUser;
  priority: TaskPriority;
  timeTotalLearning: number;
  user_task: number;
  categoryId: number;
}

export interface TaskRequestDTO {
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  status: StatusUser;
  priority: TaskPriority;
  timeTotalLearning: number;
  user_task: number;
  categoryId: number;
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = '/api/task';

  constructor(private http: HttpClient) { }

  getTasks(): Observable<Task[]> {
    return this.http.get<Page<Task>>(this.apiUrl).pipe(
      map(response => response.content)
    );
  }

  createTask(task: TaskRequestDTO): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task);
  }

  updateTask(id: number, task: TaskRequestDTO): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${id}`, task);
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
