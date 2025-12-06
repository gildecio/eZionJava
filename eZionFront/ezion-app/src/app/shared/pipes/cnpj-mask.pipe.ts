import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'cnpjMask',
  standalone: true
})
export class CnpjMaskPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) return '';
    
    // Remove non-numeric characters
    const numericOnly = value.replace(/\D/g, '');
    
    // Apply mask: XX.XXX.XXX/XXXX-XX
    if (numericOnly.length <= 2) {
      return numericOnly;
    }
    if (numericOnly.length <= 5) {
      return `${numericOnly.slice(0, 2)}.${numericOnly.slice(2)}`;
    }
    if (numericOnly.length <= 8) {
      return `${numericOnly.slice(0, 2)}.${numericOnly.slice(2, 5)}.${numericOnly.slice(5)}`;
    }
    if (numericOnly.length <= 12) {
      return `${numericOnly.slice(0, 2)}.${numericOnly.slice(2, 5)}.${numericOnly.slice(5, 8)}/${numericOnly.slice(8)}`;
    }
    return `${numericOnly.slice(0, 2)}.${numericOnly.slice(2, 5)}.${numericOnly.slice(5, 8)}/${numericOnly.slice(8, 12)}-${numericOnly.slice(12, 14)}`;
  }
}
