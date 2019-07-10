package br.ifpe.web2.missoes.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.ifpe.web2.missoes.dao.FotoDAO;
import br.ifpe.web2.missoes.exceptions.FotoInvalidaException;
import br.ifpe.web2.missoes.exceptions.FotoNotFoundException;
import br.ifpe.web2.missoes.model.Foto;
import br.ifpe.web2.missoes.model.Funcionario;

@Service
public class FotoStorageService {

	@Autowired FotoDAO fotos;
	
    public Foto salvar(Foto foto) {
    	return fotos.save(foto);
    }

    public Foto obterPorId(String id) throws FotoNotFoundException {
        return fotos.findById(id).orElseThrow(() -> new FotoNotFoundException("Foto não encontrada: " + id));
    }
    public Foto obterPorFuncionario(Funcionario funcionario) throws FotoNotFoundException {
    	return this.fotos.findByFuncionario(funcionario).orElseThrow(() -> new FotoNotFoundException("Foto inexistente."));
    }
    
    public Foto validar(MultipartFile file) throws FotoInvalidaException {
    	if (!(file.getContentType().equals("image/jpeg") 
		       	|| file.getContentType().equals("image/gif")
		       	|| file.getContentType().equals("image/png"))) 
    	{
			throw new FotoInvalidaException("Foto em formato inválido. Formatos permitidos: JPG, GIF, PNG.");
		} else if (file.getSize() > 5242880) {
			throw new FotoInvalidaException("O tamanho máximo da foto não deve exceder 5 MB.");
		} else {
			try {
				String nomeFoto = StringUtils.cleanPath(file.getOriginalFilename());
				String tipoFoto = file.getContentType();
				byte[] conteudoFoto = file.getBytes();
				
				return new Foto(null, nomeFoto, tipoFoto, conteudoFoto);
			} catch (IOException ioe) {
				throw new FotoInvalidaException("Desculpe, algo deu errado.");
			}
		}
    }

	public boolean existeComFuncionario(Funcionario funcionario) {
		return this.fotos.existsByFuncionario(funcionario);
	}
	
}
