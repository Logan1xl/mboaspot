import { Injectable } from '@angular/core';
import { jsPDF } from 'jspdf';

export interface Receipt {
  id: string;
  date: Date;
  title: string;
  amount: number;
  currency?: string;
  items?: ReceiptItem[];
  vendor?: {
    name: string;
    address?: string;
    email?: string;
    phone?: string;
  };
  customer?: {
    name: string;
    email?: string;
    phone?: string;
  };
  notes?: string;
}

export interface ReceiptItem {
  description: string;
  quantity: number;
  unitPrice: number;
  total: number;
}

@Injectable({
  providedIn: 'root'
})
export class PdfService {

  constructor() { }

  /**
   * Génère un PDF de reçu
   */
  generateReceipt(receipt: Receipt): void {
    const doc = new jsPDF();
    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();
    let yPosition = 20;

    // En-tête
    doc.setFontSize(18);
    doc.text(receipt.title, pageWidth / 2, yPosition, { align: 'center' });
    yPosition += 15;

    // Informations du reçu
    doc.setFontSize(10);
    doc.text(`ID: ${receipt.id}`, 20, yPosition);
    doc.text(`Date: ${new Date(receipt.date).toLocaleDateString()}`, 20, yPosition + 7);
    yPosition += 20;

    // Informations du vendeur
    if (receipt.vendor) {
      doc.setFontSize(11);
      doc.text('Vendeur:', 20, yPosition);
      yPosition += 7;
      doc.setFontSize(10);
      doc.text(receipt.vendor.name, 20, yPosition);
      if (receipt.vendor.address) {
        yPosition += 5;
        doc.text(receipt.vendor.address, 20, yPosition);
      }
      yPosition += 10;
    }

    // Informations du client
    if (receipt.customer) {
      doc.setFontSize(11);
      doc.text('Client:', 20, yPosition);
      yPosition += 7;
      doc.setFontSize(10);
      doc.text(receipt.customer.name, 20, yPosition);
      yPosition += 10;
    }

    // Tableau des articles
    if (receipt.items && receipt.items.length > 0) {
      const tableData = receipt.items.map(item => [
        item.description,
        item.quantity.toString(),
        item.unitPrice.toFixed(2),
        item.total.toFixed(2)
      ]);

      // Simple table without autoTable
      doc.setFontSize(9);
      const headers = ['Description', 'Quantité', 'Prix unitaire', 'Total'];
      const colWidths = [80, 30, 40, 30];
      let xPosition = 20;

      // Headers
      headers.forEach((header, index) => {
        doc.text(header, xPosition, yPosition);
        xPosition += colWidths[index];
      });

      yPosition += 7;

      // Data
      tableData.forEach(row => {
        xPosition = 20;
        row.forEach((cell, index) => {
          doc.text(cell, xPosition, yPosition);
          xPosition += colWidths[index];
        });
        yPosition += 5;
      });

      yPosition += 5;
    }

    // Total
    doc.setFontSize(12);
    doc.text(
      `Montant total: ${receipt.currency || '€'} ${receipt.amount.toFixed(2)}`,
      pageWidth - 20,
      yPosition,
      { align: 'right' }
    );
    yPosition += 15;

    // Notes
    if (receipt.notes) {
      doc.setFontSize(10);
      doc.text('Notes:', 20, yPosition);
      yPosition += 7;
      doc.setFontSize(9);
      const notesLines = doc.splitTextToSize(receipt.notes, pageWidth - 40);
      doc.text(notesLines, 20, yPosition);
    }

    // Télécharger le PDF
    doc.save(`recu_${receipt.id}.pdf`);
  }

  /**
   * Génère un PDF simple
   */
  generateSimplePdf(title: string, content: string, fileName: string): void {
    const doc = new jsPDF();
    const pageWidth = doc.internal.pageSize.getWidth();

    doc.setFontSize(18);
    doc.text(title, pageWidth / 2, 20, { align: 'center' });

    doc.setFontSize(12);
    const lines = doc.splitTextToSize(content, pageWidth - 40);
    doc.text(lines, 20, 40);

    doc.save(`${fileName}.pdf`);
  }
}
