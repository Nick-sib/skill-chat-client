package com.example.cryptochat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cryptochat.adapter.MessageController;
import com.example.cryptochat.common.Server;
import com.example.cryptochat.message.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements UserEvents{
    private static String myName = "";


    Context mContext;
    //RecyclerView chatWindow;
    private MessageController controller;
    private Server server;
    private TextView tv_UsersOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        tv_UsersOnline = findViewById(R.id.tv_UsersOnline);
        final EditText input = new EditText(this);

        new AlertDialog.Builder(this)
            .setTitle(R.string.Invate_name)
            .setView(input)
            .setPositiveButton(R.string.text_Save, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                myName = input.getText().toString();
                server.sendName(myName);
                //addTestMessages();
                }
                })
            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    myName = "X Phantom";
                    server.sendName(myName);
                    //addTestMessages();
                }
            })
            .show();

        //server.sendName(myName);

        //chatWindow = findViewById(R.id.rv_ChatWindow);
        controller = new MessageController();

        controller
                .setIncomingLayout(R.layout.incoming_message)
                .setOutgoingLayout(R.layout.outgoing_message)
                .setMessageTextId(R.id.tv_MessageText)
                .setMessageTimeId(R.id.tv_MessageTime)
                .setUserNameId(R.id.tv_UserName)
                .appendTo((RecyclerView) findViewById(R.id.rv_ChatWindow), this);

        final EditText chatInput = findViewById(R.id.e_ChatInput);

        findViewById(R.id.sendMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = chatInput.getText().toString();
                controller.addMessage(
                        new Message(
                                text,
                                myName,
                                true)
                );
                chatInput.setText("");
                server.sendMessage(text);
            }
        });
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        server = new Server(new Consumer<Pair<String, String>>() {
            @Override
            public void accept(final Pair<String, String> pair) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.addMessage(
                                new MessageController.Message(pair.second, pair.first, false)
                        );
                    }
                });
            }
        }, new Consumer<Pair<String, String>>() {
            @Override
            public void accept(final Pair<String, String> pair) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("From %s: %s", pair.first, pair.second);
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        server.connect();
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        if (server == null)
            server = new Server(new Consumer<Pair<String, String>>() {
            @Override
            public void accept(final Pair<String, String> pair) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.addMessage(
                                new Message(
                                        pair.second,
                                        pair.first,
                                        false)
                        );
                    }
                });
            }
        }/*, new Consumer<Pair<String, String>>() {
            @Override
            public void accept(Pair<String, String> pair) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("From %s: %s", pair.first, pair.second);
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }*/);
        server.connect();
        server.setonUserEventsListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        server.disconnect();
    }

   /* private void addTestMessages() {
        controller.addMessage(
                new Message(
                        "Всем приветы в этом чате! Очень рад узнать наконец как разрабатываются приложения под Android=)))))",
                        "Лунатик",
                        false)
        );
        controller.addMessage(
                new Message(
                        "И тебе привет",
                        myName,
                        true)
        );
    }*/

    /*@Override
    protected void onStart() {
        super.onStart();
        server = new Server(new Consumer<Pair<String, String>>() {
            @Override
            public void accept(final Pair<String, String> pair) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.addMessage(
                                new Message(pair.second, pair.first, false)
                        );
                    }
                });
            }
        }, new Consumer<Pair<String, String>>() {
            @Override
            public void accept(final Pair<String, String> pair) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("From %s: %s", pair.first, pair.second);
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        server.connect();
    }*/



    @SuppressLint("StringFormatMatches")
    @Override
    public void UserCONNECED(final String userName, final int totalCount) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              tv_UsersOnline.setText(String.format(getString(R.string.text_usersonline), totalCount));

                              Toast.makeText(mContext, "User: " + userName + " CONNECED",  Toast.LENGTH_LONG).show();
                          }
                      });
        /*Looper.prepare();
        tv_UsersOnline.setText(String.format(getString(R.string.text_usersonline), totalCount));
        //Log.d("myLOG", String.format(getString(R.string.text_usersonline), totalCount));
        Toast.makeText(this, "User: " + userName + " CONNECED",  Toast.LENGTH_LONG).show();
        Looper.loop();*/
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void UserDISCONNECED(final int totalCount) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              tv_UsersOnline.setText(String.format(getString(R.string.text_usersonline), totalCount));

                          }
                      }   );

        /*Looper.prepare();
        tv_UsersOnline.setText(String.format(getString(R.string.text_usersonline), totalCount));
        Looper.loop();*/
    }
}
