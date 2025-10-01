import { Component, OnInit } from '@angular/core';
import { Goal, GoalService } from '../../services/goal.service';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
// I will create GoalFormComponent later
// import { GoalFormComponent } from '../goal-form/goal-form.component';

@Component({
  selector: 'app-goal-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule
  ],
  templateUrl: './goal-list.component.html',
  styleUrls: ['./goal-list.component.css']
})
export class GoalListComponent implements OnInit {

  goals: Goal[] = [];
  displayedColumns: string[] = ['id', 'title', 'description', 'type', 'goalValue', 'goalActual', 'endDate', 'active', 'actions'];

  constructor(
    private goalService: GoalService,
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.loadGoals();
  }

  loadGoals(): void {
    this.goalService.getGoals().subscribe(goals => {
      this.goals = goals;
    });
  }

  openGoalForm(goal?: Goal): void {
    // const dialogRef = this.dialog.open(GoalFormComponent, {
    //   width: '400px',
    //   data: goal
    // });

    // dialogRef.afterClosed().subscribe(result => {
    //   if (result) {
    //     if (goal) {
    //       // Update
    //       this.goalService.updateGoal(goal.id, result).subscribe(() => {
    //         this.loadGoals();
    //       });
    //     } else {
    //       // Create
    //       this.goalService.createGoal(result).subscribe(() => {
    //         this.loadGoals();
    //       });
    //     }
    //   }
    // });
  }

  deleteGoal(id: number): void {
    if (confirm('Are you sure you want to delete this goal?')) {
      this.goalService.deleteGoal(id).subscribe(() => {
        this.loadGoals();
      });
    }
  }
}
