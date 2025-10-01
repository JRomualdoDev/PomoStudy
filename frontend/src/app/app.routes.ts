import { Routes } from '@angular/router';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { LayoutComponent } from './components/layout/layout.component';
import { authGuard } from './auth.guard';
import {GoalListComponent} from "./components/goal-list/goal-list.component";

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        redirectTo: 'categories',
        pathMatch: 'full'
      },
      {
        path: 'categories',
        component: CategoryListComponent
      },
      {
        path: 'tasks',
        component: TaskListComponent
      },
      {
        path: 'users',
        component: UserListComponent
      },
      {
        path: 'goals',
        component: GoalListComponent
      }
    ]
  }
];
