import { Snackbar } from "/plugins/notification/snackbar/snackbar.min.js"

export function showSnackBar(messages: { success?: string; error?: string; }) {
  const args: any = {
    actionText: 'Bỏ qua',
    actionTextColor: '#3b3f5c'
  };
  if (messages.success) {
    args.text = messages.success;
    args.textColor = '#ddf5f0';
    args.backgroundColor = '#00ab55';
  } else if (messages.error) {
    args.text = messages.error;
    args.textColor = '#fbeced';
    args.backgroundColor = '#e7515a';
  } else return;
  Snackbar.show(args);
}