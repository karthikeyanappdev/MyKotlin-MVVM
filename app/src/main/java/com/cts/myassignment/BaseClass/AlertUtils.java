/**
 * @author Cognizant
 * @brief Defines the global constants used across the application
 * @copyright Copyright (c) 2015 Cognizant. All rights reserved.
 * @version 1.0
 */
package com.cts.myassignment.BaseClass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.fairprice.mcomapp.R;
import com.fairprice.mcomapp.common.application.FPApplication;
import com.fairprice.mcomapp.login.views.FPForgotPasswordActivity;
import com.fairprice.mcomapp.splash.view.FPSplashActivity;


public class AlertUtils {

    private static final int MESSAGE_ALERT = 1;
    private static final int MESSAGE_ALERT_CALLBACK = 4;
    private static final int CONFIRM_ALERT = 2;
    private static final int DECISION_ALERT = 3;

    // static AlertDialog dialog = null;

    public static void messageAlert(Context ctx, String title, String message) {
        if (ctx != null) {
            showAlertDialog(MESSAGE_ALERT, ctx, title, message, null, true,
                    ctx.getString(R.string.ok_string));
        }
    }

    public static void messageAlertCustomButtonTxt(Context ctx, String title, String message, String buttonTxt) {
        if (ctx != null) {
            showAlertDialog(MESSAGE_ALERT, ctx, title, message, null, true,
                    buttonTxt);
        }
    }


    public static void confirmationAlert1(Context ctx, String title,
                                          String message, DialogInterface.OnClickListener callBack) {
        if (ctx != null) {
            showAlertDialog(CONFIRM_ALERT, ctx, title, message, callBack, true,
                    ctx.getString(R.string.ok_string),
                    ctx.getString(R.string.cancel));
        }
    }


    public static void messageAlert(Context ctx, String title,
                                    String message, DialogInterface.OnClickListener callBack) {
        if (ctx != null) {
            showAlertDialog(MESSAGE_ALERT_CALLBACK, ctx, title, message, callBack, false,
                    ctx.getString(R.string.ok_string)
            );
        }
    }


    public static void decisionAlert(Context ctx, String title, String message,
                                     DialogInterface.OnClickListener posCallback, String... buttonNames) {
        if (ctx != null) {
            showAlertDialog(DECISION_ALERT, ctx, title, message, posCallback, true,
                    buttonNames);
        }
    }


    public static void decisionAlert(Context ctx, String title, String message,
                                     DialogInterface.OnClickListener posCallback, boolean closeOnTouchOutside, String... buttonNames) {
        if (ctx != null) {
            showAlertDialog(DECISION_ALERT, ctx, title, message, posCallback, closeOnTouchOutside,
                    buttonNames);
        }

    }

    private static void showAlertDialog(int alertType, final Context ctx,
                                        String title, String message, final DialogInterface.OnClickListener posCallback, boolean closeOnTouchOutside, String... buttonNames) {
        if (ctx != null) {
            if (title == null)
                title = ctx.getResources().getString(R.string.alert_name);
            if (message == null)
                return;

            final Dialog dialog = new Dialog(ctx);
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogLayout = inflater.inflate(R.layout.custom_alert_dialog, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogLayout);
            dialog.setCancelable(false);


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            TextView customAlertTitle = (TextView) dialogLayout.findViewById(R.id.title);
            TextView customAlertMessage = (TextView) dialogLayout.findViewById(R.id.message);
            Button customAlertPositiveButton = (Button) dialogLayout.findViewById(R.id.positive_button);
            Button customAlertNegativeButton = (Button) dialogLayout.findViewById(R.id.negative_button);
            View vertical_divider = (View) dialogLayout.findViewById(R.id.line);
            customAlertTitle.setText(title);
            customAlertMessage.setText(Html.fromHtml(message));
            customAlertPositiveButton.setText(buttonNames[0]);
            customAlertNegativeButton.setText(buttonNames[buttonNames.length - 1]);


            switch (alertType) {
                case MESSAGE_ALERT:
                    customAlertNegativeButton.setVisibility(View.GONE);
                    vertical_divider.setVisibility(View.GONE);
                    customAlertPositiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ctx instanceof FPSplashActivity) {
                                dialog.dismiss();
                            } else if (ctx instanceof FPForgotPasswordActivity) {
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
                    /*dialog.setNegativeButton(buttonNames[buttonNames.length - 1],
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface,
                                                    int i) {

                                    if (ctx instanceof FPSplashActivity) {
                                        dialogInterface.dismiss();
//                                        ((FPSplashActivity) ctx).finish();

                                    } else if (ctx instanceof FPForgotPasswordActivity) {
                                        dialogInterface.dismiss();
                                    } else {
                                        dialogInterface.dismiss();
                                    }

                                }
                            });*/
                    break;
                case DECISION_ALERT:
                    customAlertNegativeButton.setVisibility(View.VISIBLE);
                    vertical_divider.setVisibility(View.VISIBLE);

                case CONFIRM_ALERT:
                    customAlertNegativeButton.setVisibility(View.VISIBLE);
                    vertical_divider.setVisibility(View.VISIBLE);


                    customAlertPositiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (posCallback != null) {
                                posCallback.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            }
                        }
                    });

                    customAlertNegativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (posCallback != null) {
                                posCallback.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            }
                        }
                    });

                    break;
                case MESSAGE_ALERT_CALLBACK:
                    customAlertNegativeButton.setVisibility(View.GONE);
                    vertical_divider.setVisibility(View.GONE);

                    customAlertPositiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (posCallback != null) {
                                posCallback.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            }
                        }
                    });
                    break;
            }
            dialog.show();

           /* AlertDialog alertDialog = builder.create();
            lp.copyFrom(alertDialog.getWindow().getAttributes());
            lp.width = 150;
            lp.height = 500;
            alertDialog.getWindow().setAttributes(lp);
            alertDialog.show();
            if (closeOnTouchOutside) {
                alertDialog.setCanceledOnTouchOutside(true);
            }

            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                    if (ctx instanceof FPSplashActivity) {
//                       ((FPSplashActivity) ctx).finish();
                    }
                }
            });*/
//            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    Log.e("context value 6", "dismiss listener activity");
//                    if(ctx instanceof FPSplashActivity){
//                        ((FPSplashActivity) ctx).finish();
//                    }
//                }
//            });
        }
    }


    /*Alert dialog with "Yes" & "No" option for mandarin products*/
    public static void alertDialog(final Context mContext,
                                   String title, String message,

                                   DialogInterface.OnClickListener posCallback, boolean closeOnTouchOutside, String... buttonNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.layout_mandarin_orange_popup, null);
        TextView heading = (TextView) view.findViewById(R.id.content_heading);
        TextView content = (TextView) view.findViewById(R.id.content);

        heading.setText(title);
        content.setText(message);
        builder.setView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        builder.setPositiveButton(buttonNames[0], posCallback);
        builder.setNegativeButton(buttonNames[buttonNames.length - 1],
                posCallback);
        final AlertDialog mDialogDetails = builder.create();

        mDialogDetails.setCancelable(false);
        mDialogDetails.show();
        if (FPApplication.IS_TABLET) {
            Configuration newConfig = mContext.getResources().getConfiguration();
            lp.copyFrom(mDialogDetails.getWindow().getAttributes());
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                lp.width = (int) (FPAppUtils.getScreenWidthResolution(mContext) * 0.5);
            } else {
                lp.width = (int) (FPAppUtils.getScreenWidthResolution(mContext) * 0.8);
            }
            mDialogDetails.getWindow().setAttributes(lp);
        }
        if (closeOnTouchOutside) {
            mDialogDetails.setCanceledOnTouchOutside(true);
        }
    }

    /**
     * Method to show dialog which will Auto close without user action after 3s
     * @param ctx
     * @param title
     * @param message
     * @param closeOnTouchOutside
     * @param isClose
     * @param posCallback
     */
    public static void showDialogWithAutoClose(Context ctx, String title, String message, boolean closeOnTouchOutside, boolean isClose, DialogInterface.OnClickListener posCallback) {
        if (ctx != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            builder.setTitle(Html.fromHtml(title)).setMessage(Html.fromHtml(message)).setCancelable(false);
            builder.setNegativeButton(ctx.getString(R.string.ok_string), posCallback);


            final AlertDialog alertDialog = builder.create();
            lp.copyFrom(alertDialog.getWindow().getAttributes());
            lp.width = 150;
            lp.height = 500;
            alertDialog.getWindow().setAttributes(lp);
            alertDialog.show();
            if (closeOnTouchOutside) {
                alertDialog.setCanceledOnTouchOutside(true);
            }
            if (isClose) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            alertDialog.dismiss();
                        } catch (IllegalArgumentException arg) {
                            arg.printStackTrace();
                        }
                    }
                }, 3000);
            }
        }
    }


    public static void showDialogWithHTMLText(final Context ctx, String title, boolean noTitle, String message, DialogInterface.OnClickListener posCallback, String... buttonNames) {
        if (ctx != null) {
            final Dialog dialog = new Dialog(ctx);
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogLayout = inflater.inflate(R.layout.custom_alert_dialog, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogLayout);
            dialog.setCancelable(false);


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            TextView customAlertTitle = (TextView) dialogLayout.findViewById(R.id.title);
            TextView customAlertMessage = (TextView) dialogLayout.findViewById(R.id.message);
            Button customAlertPositiveButton = (Button) dialogLayout.findViewById(R.id.positive_button);
            Button customAlertNegativeButton = (Button) dialogLayout.findViewById(R.id.negative_button);
            View vertical_divider = (View) dialogLayout.findViewById(R.id.line);
            customAlertTitle.setText(title);
            if (noTitle) {
                customAlertTitle.setVisibility(View.GONE);
            }
            FPAppUtils.setHtmlText(customAlertMessage, message);
            customAlertPositiveButton.setText(buttonNames[0]);
            customAlertNegativeButton.setText(buttonNames[buttonNames.length - 1]);
            customAlertNegativeButton.setVisibility(View.GONE);
            vertical_divider.setVisibility(View.GONE);
            customAlertPositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ctx instanceof FPSplashActivity) {
                        dialog.dismiss();
                    } else if (ctx instanceof FPForgotPasswordActivity) {
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

    public static void showCouponsTermsPopup(final Context ctx, String title, boolean noTitle, String message, View.OnClickListener posCallback, String... buttonNames) {
        if (ctx != null) {
            final Dialog dialog = new Dialog(ctx);
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogLayout = inflater.inflate(R.layout.custom_alert_dialog_coupon_tc, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogLayout);
            dialog.setCancelable(false);


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            TextView customAlertTitle = (TextView) dialogLayout.findViewById(R.id.title);
            WebView webView = (WebView) dialogLayout.findViewById(R.id.message);
            Button customAlertPositiveButton = (Button) dialogLayout.findViewById(R.id.positive_button);
            Button customAlertNegativeButton = (Button) dialogLayout.findViewById(R.id.negative_button);
            View vertical_divider = (View) dialogLayout.findViewById(R.id.line);
            customAlertTitle.setText(title);
            if (noTitle) {
                customAlertTitle.setVisibility(View.GONE);
            }
            message= message.replaceAll("<h3>","<h4>");
            message= message.replaceAll("</h3>","</h4>");

         String CSS_STYLE = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densityDpi=device-dpi\" />" +
                    "<style type=\"text/css\">@font-face {\n" +
                    "   font-family: Muli_Normal; \n" +
                    "   src: url(\"file:///android_asset/fonts/Muli_Normal.ttf\")\n" +
                    "   }" +
                    "</style>" +
                    "</head><body><div style='text-align:left; font-size:12px !important;font-family:Muli_Normal; '>";
            message = CSS_STYLE + message + "</div></body></html>";

             webView.loadDataWithBaseURL("file:///android_res/drawable/", message, "text/html", "charset=UTF-8", null);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            customAlertPositiveButton.setText(buttonNames[0]);
            customAlertNegativeButton.setText("Cancel");
          //  vertical_divider.setVisibility(View.GONE);
            customAlertPositiveButton.setOnClickListener(posCallback);
            customAlertNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }

}