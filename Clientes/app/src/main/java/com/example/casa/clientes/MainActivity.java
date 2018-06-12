package com.example.casa.clientes;

import android.app.Service;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    EditText editCodigo, editNome, editTelefone, editEmail;
    Button btnLimpar, btnSalvar, btnExcluir;
    ListView listviewClientes;

    Dao db = new Dao(this);

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCodigo = (EditText) findViewById(R.id.editCodigo);
        editNome = (EditText) findViewById(R.id.editNome);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        editEmail = (EditText) findViewById(R.id.editEmail);

        btnLimpar = (Button) findViewById(R.id.btnLimpar);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);

        imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);

        listviewClientes = (ListView) findViewById(R.id.listviewClientes);

        listarClientes();

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });

        listviewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conteudo = (String) listviewClientes.getItemAtPosition(position);

                // Toast.makeText(MainActivity.this, "Select " + conteudo, Toast.LENGTH_LONG).show();

                String codigo = conteudo.substring(0, conteudo.indexOf("_"));

                Cliente cliente = db.selecionarCliente(Integer.parseInt(codigo));

                editCodigo.setText(String.valueOf(cliente.getCodigo()));
                editNome.setText(cliente.getNome());
                editTelefone.setText(cliente.getTelefone());
                editEmail.setText(cliente.getEmail());
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = editCodigo.getText().toString();
                String nome = editNome.getText().toString();
                String telefone = editTelefone.getText().toString();
                String email = editEmail.getText().toString();

                if (nome.isEmpty()) {
                    editNome.setError("Digite o Nome.");

                    } else {
                    if (codigo.isEmpty()) {
                        //insert
                        db.addCliente(new Cliente(nome, telefone, email));
                        Toast.makeText(MainActivity.this, "Cliente adicionado com sucesso", Toast.LENGTH_LONG).show();

                        limpaCampos();
                        listarClientes();

                    } else {
                        //update
                        db.atualizaCliente(new Cliente(Integer.parseInt(codigo), nome, telefone, email));

                        Toast.makeText(MainActivity.this, "Cliente atualizado com sucesso", Toast.LENGTH_LONG).show();

                        limpaCampos();
                        listarClientes();

                    }

                }

            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = editCodigo.getText().toString();
                if (codigo.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Nenhum cliente selecionado", Toast.LENGTH_LONG).show();
                } else {
                    Cliente cliente = new Cliente();
                    cliente.setCodigo(Integer.parseInt(codigo));
                    db.apagarCliente(cliente);

                    Toast.makeText(MainActivity.this, "Cliente excluido com sucesso", Toast.LENGTH_LONG).show();

                    limpaCampos();
                    listarClientes();

                }
            }

        });
    }



    void limpaCampos(){
        editCodigo.setText("");
        editNome.setText("");
        editTelefone.setText("");
        editEmail.setText("");

        editNome.requestFocus();
    }

    public void listarClientes(){
        List<Cliente> clientes = db.listaContatos();

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);

        listviewClientes.setAdapter(adapter);


        for (Cliente c : clientes){
        //    Log.d("Lista", "\nID: " + c.getCodigo() + " nome: " + c.getNome() );
            arrayList.add(c.getCodigo() + "-" + c.getNome());
            adapter.notifyDataSetChanged();
        }
    }
}
