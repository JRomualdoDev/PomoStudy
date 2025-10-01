import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Page } from './category.service';

export enum GoalType {
  DAILY_TIME = 'DAILY_TIME',
  WEEKLY_TIME = 'WEEKLY_TIME',
  POMODORO_DAILY = 'POMODORO_DAILY',
  TASKS_COMPLETED = 'TASKS_COMPLETED'
}

export interface Goal {
  id: number;
  title: string;
  description: string;
  type: GoalType;
  goalValue: number;
  goalActual: number;
  endDate: string;
  active: boolean;
}

export interface GoalRequestDTO {
  title: string;
  description: string;
  type: GoalType;
  goalValue: number;
  goalActual: number;
  endDate: string;
  active: boolean;
  user_goal: number;
}

@Injectable({
  providedIn: 'root'
})
export class GoalService {

  private apiUrl = '/api/goal';

  constructor(private http: HttpClient) { }

  getGoals(): Observable<Goal[]> {
    return this.http.get<Page<Goal>>(this.apiUrl).pipe(
      map(response => response.content)
    );
  }

  createGoal(goal: GoalRequestDTO): Observable<Goal> {
    return this.http.post<Goal>(this.apiUrl, goal);
  }

  updateGoal(id: number, goal: GoalRequestDTO): Observable<Goal> {
    return this.http.put<Goal>(`${this.apiUrl}/${id}`, goal);
  }

  deleteGoal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
