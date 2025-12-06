import { AbstractControl, ValidationErrors, ValidatorFn, FormControl } from '@angular/forms';

/**
 * Valida se um CNPJ é válido
 * Aceita CNPJ com ou sem formatação (XX.XXX.XXX/XXXX-XX ou XXXXXXXXXXXXXX)
 * 
 * @example
 * // Sem formatação
 * control.setValidators(cnpjValidator());
 * 
 * // Com formatação
 * const cnpj = new FormControl('11.222.333/0001-81');
 * cnpj.setValidators(cnpjValidator());
 */
export function cnpjValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null; // Não valida se está vazio
    }

    const cnpj = control.value.toString().replace(/[^\d]/g, '');

    // Verifica se tem 14 dígitos
    if (cnpj.length !== 14) {
      return { cnpjInvalid: { value: control.value, message: 'CNPJ deve conter 14 dígitos' } };
    }

    // Verifica se não são todos os dígitos iguais
    if (/^(\d)\1{13}$/.test(cnpj)) {
      return { cnpjInvalid: { value: control.value, message: 'CNPJ inválido' } };
    }

    // Calcula o primeiro dígito verificador
    let tamanho = cnpj.length - 2;
    let numeros = cnpj.substring(0, tamanho);
    const digitos = cnpj.substring(tamanho);
    let soma = 0;
    let pos = tamanho - 7;

    for (let i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2) {
        pos = 9;
      }
    }

    let resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
    if (resultado !== parseInt(digitos.charAt(0), 10)) {
      return { cnpjInvalid: { value: control.value, message: 'CNPJ inválido' } };
    }

    // Calcula o segundo dígito verificador
    tamanho = tamanho + 1;
    numeros = cnpj.substring(0, tamanho);
    soma = 0;
    pos = tamanho - 7;

    for (let i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2) {
        pos = 9;
      }
    }

    resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
    if (resultado !== parseInt(digitos.charAt(1), 10)) {
      return { cnpjInvalid: { value: control.value, message: 'CNPJ inválido' } };
    }

    return null;
  };
}

/**
 * Função auxiliar para validar CNPJ diretamente
 * Útil para validações fora de formulários
 * 
 * @example
 * if (isValidCNPJ('11.222.333/0001-81')) {
 *   console.log('CNPJ válido');
 * }
 */
export function isValidCNPJ(cnpj: string): boolean {
  const validator = cnpjValidator();
  const control = new FormControl(cnpj);
  return validator(control) === null;
}
