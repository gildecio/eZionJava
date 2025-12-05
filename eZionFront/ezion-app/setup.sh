#!/bin/bash

# Script de inicialização do projeto eZion Frontend

echo "================================================"
echo "  eZion Frontend - Setup Rápido"
echo "================================================"
echo ""

# Verificar se Node.js está instalado
if ! command -v node &> /dev/null; then
    echo "❌ Node.js não está instalado"
    echo "Visite: https://nodejs.org/"
    exit 1
fi

echo "✅ Node.js detectado: $(node --version)"
echo "✅ npm detectado: $(npm --version)"
echo ""

# Navegar para o diretório do projeto
cd "$(dirname "$0")" || exit

# Instalar dependências (se não existirem)
if [ ! -d "node_modules" ]; then
    echo "📦 Instalando dependências..."
    npm install
    echo "✅ Dependências instaladas"
    echo ""
fi

# Menu de opções
echo "Escolha uma opção:"
echo "1) Iniciar servidor de desenvolvimento (localhost:4200)"
echo "2) Compilar para produção"
echo "3) Rodar testes"
echo "4) Limpar build"
echo ""
read -p "Digite a opção (1-4): " option

case $option in
    1)
        echo ""
        echo "🚀 Iniciando servidor de desenvolvimento..."
        echo "   Acesse: http://localhost:4200"
        echo "   Pressione Ctrl+C para parar"
        echo ""
        npm start
        ;;
    2)
        echo ""
        echo "🏗️  Compilando para produção..."
        npm run build
        echo "✅ Build concluído em: dist/ezion-app"
        ;;
    3)
        echo ""
        echo "🧪 Executando testes..."
        npm test
        ;;
    4)
        echo ""
        echo "🧹 Limpando arquivos de build..."
        rm -rf dist/ .angular/
        echo "✅ Limpeza concluída"
        ;;
    *)
        echo "❌ Opção inválida"
        exit 1
        ;;
esac

echo ""
echo "================================================"
