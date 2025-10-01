import { Component, OnInit } from '@angular/core';
import { Category, CategoryService, CategoryRequestDTO } from '../../services/category.service';
import { CommonModule } from '@angular/common';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmTableModule } from '@spartan-ng/ui-table-helm';
import { HlmDialogModule } from '@spartan-ng/ui-dialog-helm';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [
    CommonModule,
    HlmButtonDirective,
    HlmIconComponent,
    HlmTableModule,
    HlmDialogModule
  ],
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {

  categories: Category[] = [];
  displayedColumns: string[] = ['id', 'name', 'color', 'icon', 'actions'];

  constructor(
    private categoryService: CategoryService,
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe(categories => {
      this.categories = categories;
    });
  }

  openCategoryForm(category?: Category): void {
    const dialogRef = this.dialog.open(CategoryFormComponent, {
      width: '400px',
      data: category
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (category) {
          // Update
          this.categoryService.updateCategory(category.id, result).subscribe(() => {
            this.loadCategories();
          });
        } else {
          // Create
          this.categoryService.createCategory(result).subscribe({
            next: () => {
              this.loadCategories();
            },
            error: (err) => {
              console.error(err);
              alert(err.message);
            }
          });
        }
      }
    });
  }

  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.deleteCategory(id).subscribe(() => {
        this.loadCategories();
      });
    }
  }
}
