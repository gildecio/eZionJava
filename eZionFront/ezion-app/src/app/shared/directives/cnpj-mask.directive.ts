import { Directive, HostListener, ElementRef, OnInit } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[appCnpjMask]',
  standalone: true
})
export class CnpjMaskDirective implements OnInit {
  constructor(
    private el: ElementRef,
    private ngControl: NgControl
  ) {}

  ngOnInit() {
    if (this.ngControl?.control?.value) {
      this.formatCNPJ();
    }
  }

  @HostListener('input', ['$event'])
  onInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = input.value;
    
    // Remove non-numeric characters
    const numericOnly = value.replace(/\D/g, '').slice(0, 14);
    
    // Apply mask
    let formatted = '';
    if (numericOnly.length > 0) {
      formatted = numericOnly.slice(0, 2);
    }
    if (numericOnly.length > 2) {
      formatted += '.' + numericOnly.slice(2, 5);
    }
    if (numericOnly.length > 5) {
      formatted += '.' + numericOnly.slice(5, 8);
    }
    if (numericOnly.length > 8) {
      formatted += '/' + numericOnly.slice(8, 12);
    }
    if (numericOnly.length > 12) {
      formatted += '-' + numericOnly.slice(12, 14);
    }
    
    // Update input value
    input.value = formatted;
    
    // Update form control with only numeric value
    if (this.ngControl?.control) {
      this.ngControl.control.setValue(numericOnly, { emitEvent: false });
    }
  }

  @HostListener('blur')
  onBlur(): void {
    this.formatCNPJ();
  }

  private formatCNPJ(): void {
    if (this.ngControl?.control?.value) {
      const input = this.el.nativeElement;
      const value = this.ngControl.control.value;
      const numericOnly = String(value).replace(/\D/g, '').slice(0, 14);
      
      let formatted = '';
      if (numericOnly.length > 0) {
        formatted = numericOnly.slice(0, 2);
      }
      if (numericOnly.length > 2) {
        formatted += '.' + numericOnly.slice(2, 5);
      }
      if (numericOnly.length > 5) {
        formatted += '.' + numericOnly.slice(5, 8);
      }
      if (numericOnly.length > 8) {
        formatted += '/' + numericOnly.slice(8, 12);
      }
      if (numericOnly.length > 12) {
        formatted += '-' + numericOnly.slice(12, 14);
      }
      
      input.value = formatted;
    }
  }
}
