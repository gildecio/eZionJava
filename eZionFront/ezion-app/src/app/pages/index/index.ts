import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './index.html',
  styleUrl: './index.css'
})
export class Index {
  constructor() {}
}
